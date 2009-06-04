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

import java.lang.reflect.Method;

import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.aspectj.lang.reflect.MethodSignature;
import org.jdbcluster.exception.PrivilegeException;
import org.jdbcluster.metapersistence.annotation.NoPrivilegeCheck;
import org.jdbcluster.metapersistence.cluster.ClusterBase;
import org.jdbcluster.metapersistence.security.user.IUser;
import org.jdbcluster.service.PrivilegedService;

/**
 * used to check privileges
 * @author FaKod
 */
public aspect PrivilegeAspect {

	pointcut execClusterMethod(ClusterBase c):
		execution( !@NoPrivilegeCheck public * PrivilegedCluster+.*(..)) &&
		target(c);
	
	pointcut execServiceMethod(PrivilegedService ser):
		execution( !@NoPrivilegeCheck public * PrivilegedService+.*(..)) &&
		target(ser);
	
	@SuppressAjWarnings("adviceDidNotMatch")
	before(ClusterBase c) : execClusterMethod(c) {
		PrivilegeChecker pc = PrivilegeCheckerImpl.getInstance();
		
		MethodSignature sig = (MethodSignature) thisJoinPoint.getSignature();
		Method m = sig.getMethod();
		
		IUser user = null;
		
			user = c.getUser();
		if(!pc.userPrivilegeIntersect(user, (PrivilegedCluster) c, m, thisJoinPoint.getArgs()))
			throw new PrivilegeException("unsufficient privileges on Cluster: " +
					c.getClass().getName()+
					" calling method: " +
					m.getName());
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	before(PrivilegedService ser) : execServiceMethod(ser) {
		PrivilegeChecker pc = PrivilegeCheckerImpl.getInstance();
		
		MethodSignature sig = (MethodSignature) thisJoinPoint.getSignature();
		Method m = sig.getMethod();	
		if(!pc.userPrivilegeIntersect(ser.getUser(), ser, m, thisJoinPoint.getArgs())) 
			throw new PrivilegeException("unsufficient privileges on Service: " +
					ser.getClass().getName()+
					" calling service method: " +
					m.getName());
	}

}
