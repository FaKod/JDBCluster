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
package org.jdbcluster.metapersistence.aspects;

import java.lang.reflect.Field;

import org.aspectj.lang.reflect.FieldSignature;
import org.jdbcluster.domain.DomainChecker;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.cluster.AssocCluster;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
import org.jdbcluster.JDBClusterUtil;

/**
 * @author Christopher Schmidt
 * Aspect for checking Domain values in Set Methods
 */
public aspect DomainCheck extends ClusterBaseAspect {
	
	declare precedence: DomainCheck, ClusterAttribute, *;
	
	pointcut setAttribute(ClusterBase c, String s):
		(set(* Cluster+.*) || set(* AssocCluster+.*)) && 
		!set(CSet+ Cluster+.*) && @annotation(DomainDependancy) && target(c) && args(s);
	
	Object around(ClusterBase c, String s):setAttribute(c, s) {
		DomainChecker dc = DomainChecker.getDomainChecker();
		FieldSignature sig = (FieldSignature) thisJoinPoint.getSignature();
		Field fField = sig.getField();
		DomainDependancy dd = fField.getAnnotation(DomainDependancy.class);
		try {
			Field dependsFromProperty = c.getClass().getDeclaredField(dd.dependsFromProperty());
			
			String masterDomainId;
			Domain d = dependsFromProperty.getAnnotation(Domain.class);
			if(d!=null)
				masterDomainId = d.domainId();
			else {
				DomainDependancy dd2 = dependsFromProperty.getAnnotation(DomainDependancy.class);
				masterDomainId = dd2.domainId();
			}	
			
			//String masterValue = (String) dependsFromProperty.get(c);
			String masterValue = (String) JDBClusterUtil.invokeGetPropertyMethod(dd.dependsFromProperty(), c);
			if(!dc.check(masterDomainId, masterValue, dd.domainId(), s)) {
				throw new DomainException("Value: " + s + " is not valid for slave domain " +
						dd.domainId() + " and value " + masterValue + " in master domain " +
						masterDomainId);
			}
		}
		catch(NoSuchFieldException e) {
			throw new ConfigurationException("No Such Property: " + dd.dependsFromProperty(), e);
		}
		
		Object o = proceed(c, s);
		return o;
	}
}
