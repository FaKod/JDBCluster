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
import java.util.Set;

import org.jdbcluster.metapersistence.security.user.IUser;
import org.jdbcluster.service.PrivilegedService;

/**
 * Main interface for handling privileges
 * @author FaKod
 *
 */
public interface PrivilegeChecker {
	
	/**
	 * should do an intersection between the user privileges and
	 * the privileges given through requiredPrivileges
	 * @param requiredPrivileges the required privileges
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, Set<String> requiredPrivileges);

	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedCluster clusterObject, Method calledMethod, Object... args);
	
	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param serviceObject service object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedService serviceObject, Method calledMethod, Object... args);
	
	/**
	 * intersects required privileges without a method call (new(..)) against given privileges
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(IUser user, PrivilegedCluster clusterObject);
	
	/**
	 * intersects a specific domain value with the needed rights for this value
	 * @param domainId configured domain id
	 * @param value the value of the domain
	 * @return true if the user rights are sufficient
	 */
	public boolean userPrivilegeIntersectDomain(IUser user, String domainId, String value);
	
	/**
	 * intersects required privileges against given privileges
	 * @param serviceObject service object to check
	 * @param serviceMethodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, String serviceMethodName, Object... args);
	
	/**
	 * intersects required privileges against given privileges
	 * @param serviceObject service object to check
	 * @param serviceMethodName method name to check
	 * @param args array of parameter
	 * @param argTypes array of parameter class objects
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, String serviceMethodName, Object[] args, Class[] argTypes);
	
	/**
	 * intersects required privileges against given privileges
	 * @param serviceObject service object to check
	 * @param serviceMethod method name to check
	 * @param args array of parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedService serviceObject, Method serviceMethod, Object... args);
	/**
	 * intersects required privileges against given privileges
	 * @param clusterObject cluster object instance
	 * @param methodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, String methodName, Object... args);
	
	/**
	 * intersects required privileges against given privileges
	 * @param clusterObject cluster object instance
	 * @param methodName method name to check
	 * @param args array of parameter
	 * @param argTypes array of parameter class objects
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, String methodName, Object[] args, Class[] argTypes);
	
	/**
	 * intersects required privileges against given privileges
	 * @param clusterObject cluster object instance
	 * @param method method name to check
	 * @param args array of parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(IUser user, PrivilegedCluster clusterObject, Method method, Object... args);
	
	/**
	 * intersects required static privileges for cluster new against given
	 * privileges
	 * 
	 * @param clusterType defines the Cluster to check
	 * @return true if the privileges are sufficient
	 */
	public boolean checkClusterNew(IUser user, String clusterType);
}
