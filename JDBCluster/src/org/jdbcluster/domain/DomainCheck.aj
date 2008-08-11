/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.domain;

import java.lang.reflect.Field;
import java.util.Set;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.FieldSignature;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.exception.PrivilegeException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;
import org.jdbcluster.metapersistence.aspects.ClusterAttribute;
import org.jdbcluster.metapersistence.cluster.AssocCluster;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.privilege.PrivilegeChecker;
import org.jdbcluster.privilege.PrivilegeCheckerImpl;

/**
 * Aspect for checking Domain values in Set Methods
 * declares precedence to run of this aspect first (!!)
 * @author FaKod
 * @author Christopher Schmidt
 */
public aspect DomainCheck {
	
	declare precedence: DomainCheck, ClusterAttribute, *;
	
	pointcut setDepAttribute(Cluster c, String s):
		(set(* Cluster+.*) || set(* AssocCluster+.*)) && 
		!set(CSet+ Cluster+.*) && 
		@annotation(DomainDependancy) && 
		!@annotation(Domain) &&
		target(c) && args(s);
	
	pointcut setAttribute(Cluster c, String s):
		(set(* Cluster+.*) || set(* AssocCluster+.*)) && 
		!set(CSet+ Cluster+.*) && 
		@annotation(Domain) &&
		!@annotation(DomainDependancy) && 
		target(c) && args(s);
	
	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(Cluster c, String s):setDepAttribute(c, s) {
		DomainCheckerImpl dc = DomainCheckerImpl.getImplInstance();
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		DomainDependancy dd = fField.getAnnotation(DomainDependancy.class);
		
		if(fField.isAnnotationPresent(PrivilegesDomain.class))
			if(!checkPrivileges(dd.domainId(), s))
				throw new PrivilegeException("unsufficient privileges on Privileged Domain: " +
						c.getClass().getName()+
						" writing value "+ s + " to field: " +
						fField.getName());
		
		if(!dc.check(c, fField, s, dd))
			throw new DomainException("Value: " + s + " is not valid for slave domain " + dd.domainId() + 
					" in Class " + c.getClass().getName() +
					" in Field " + fField.getName());
		
		Object o = proceed(c, s);
		return o;
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	Object around(Cluster c, String s):setAttribute(c, s) {
		DomainCheckerImpl dc = DomainCheckerImpl.getImplInstance();
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		Domain d = fField.getAnnotation(Domain.class);
		
		if(fField.isAnnotationPresent(PrivilegesDomain.class))
			if(!checkPrivileges(d.domainId(), s))
				throw new PrivilegeException("unsufficient privileges on Privileged Domain: " +
						c.getClass().getName()+
						" writing value "+ s + " to field: " +
						fField.getName());
		
		if(!dc.checkAgainstPossibleDomainEntries(d.domainId(), s))
			throw new DomainException("Value: " + s + " is not valid for domain " + d.domainId()+ 
					" in Class " + c.getClass().getName() +
					" in Field " + fField.getName());
		
		Object o = proceed(c, s);
		return o;
	}
	
	private static boolean checkPrivileges(String domainId, String value) {
		DomainCheckerImpl dc = DomainCheckerImpl.getImplInstance();
		PrivilegeChecker pc = PrivilegeCheckerImpl.getInstance();
		DomainPrivilegeList dpl;
		try {
			dpl = (DomainPrivilegeList) dc.getDomainListInstance(domainId);
		} catch (ClassCastException e) {
			throw new ConfigurationException("privileged domain [" + domainId + "] needs implemented DomainPrivilegeList Interface", e);
		}
		Set<String> rights = dpl.getDomainEntryPivilegeList(domainId, value);
		return pc.userPrivilegeIntersect(rights);
	}
}
