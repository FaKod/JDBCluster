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
package org.jdbcluster.privilege;

import org.jdbcluster.metapersistence.annotation.PrivilegesParameter;

import org.jdbcluster.metapersistence.annotation.PrivilegesParameter;
import org.aspectj.lang.reflect.MethodSignature;
import org.jdbcluster.exception.PrivilegeException;
import org.jdbcluster.metapersistence.annotation.NoPrivilegeCheck;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
import org.jdbcluster.service.PrivilegedService;
import java.lang.reflect.Method;

/**
 * used to check privileges
 * @author FaKod
 */
public privileged aspect PrivilegeAspect {

	pointcut execClusterMethod(ClusterBase c):
		execution( !@NoPrivilegeCheck public * PrivilegedCluster+.*(..)) &&
		target(c);
	
	pointcut execServiceMethod(PrivilegedService ser):
		execution( !@NoPrivilegeCheck public * PrivilegedService+.*(..)) &&
		target(ser);
	
	pointcut execServiceMethod1Param(PrivilegedService ser, PrivilegedCluster pc):
		execution( !@NoPrivilegeCheck public * PrivilegedService+.*( @PrivilegesParameter PrivilegedCluster+)) &&
		target(ser) && args(pc);
	
	before(PrivilegedService ser, PrivilegedCluster pc) : execServiceMethod1Param(ser, pc) {
		
	}
	
	before(ClusterBase c) : execClusterMethod(c) {
		PrivilegeCheckerImpl pc = new PrivilegeCheckerImpl();
		
		MethodSignature sig = (MethodSignature) thisJoinPoint.getSignature();
		Method m = sig.getMethod();	
		
		if(!pc.userPrivilegeIntersect((PrivilegedCluster) c, m, new Object[0])) 
			throw new PrivilegeException("unsufficient privileges on Cluster: " +
					c.getClass().getName()+
					" calling method: " +
					m.getName());
	}
	
	before(PrivilegedService ser) : execServiceMethod(ser) {
		PrivilegeCheckerImpl pc = new PrivilegeCheckerImpl();
		
		MethodSignature sig = (MethodSignature) thisJoinPoint.getSignature();
		Method m = sig.getMethod();	
		
		if(!pc.userPrivilegeIntersect(ser, m, new Object[0])) 
			throw new PrivilegeException("unsufficient privileges on Service: " +
					ser.getClass().getName()+
					" calling service method: " +
					m.getName());
	}

}
