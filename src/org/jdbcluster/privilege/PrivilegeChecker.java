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
	public boolean userPrivilegeIntersect(Set<String> requiredPrivileges);

	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(PrivilegedCluster clusterObject, Method calledMethod, Object... args);
	
	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param serviceObject service object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(PrivilegedService serviceObject, Method calledMethod, Object... args);
	
	/**
	 * intersects required privileges without a method call (new(..)) against given privileges
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public boolean userPrivilegeIntersect(PrivilegedCluster clusterObject);
	
	/**
	 * intersects required privileges against given privileges
	 * @param serviceObject service object to check
	 * @param serviceMethodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(PrivilegedService serviceObject, String serviceMethodName, Object... args);
	
	/**
	 * intersects required privileges against given privileges
	 * @param clusterObject
	 * @param methodName method name to check
	 * @param args of method parameter
	 * @return true if the privileges are sufficient
	 */
	public boolean checkAccess(PrivilegedCluster clusterObject, String methodName, Object... args);
	
	/**
	 * intersects required static privileges for cluster new against given
	 * privileges
	 * 
	 * @param clusterType defines the Cluster to check
	 * @return true if the privileges are sufficient
	 */
	public boolean checkClusterNew(String clusterType);
}
