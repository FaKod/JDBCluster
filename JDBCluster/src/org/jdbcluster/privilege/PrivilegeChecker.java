package org.jdbcluster.privilege;

import java.lang.reflect.Method;
import java.util.Set;

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
	public abstract boolean userPrivilegeIntersect(Set<String> requiredPrivileges);

	/**
	 * intersects required privileges against given privileges
	 * @param calledMethod the method called
	 * @param clusterObject cluster object instance
	 * @return true if the privileges are sufficient
	 */
	public abstract boolean userPrivilegeIntersect(Method calledMethod, PrivilegedCluster clusterObject);
}
