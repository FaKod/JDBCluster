package org.jdbcluster.privilege;

import java.util.Set;

/**
 * used be the privilege checker to validate privileges
 * @author FaKod
 *
 */
public interface UserPrivilegeChecker {

	/**
	 * should do an intersection between the user privileges and
	 * the privileges given through requiredPrivileges
	 * @param requiredPrivileges the required privileges
	 * @return true if the privileges are sufficient
	 */
	public abstract boolean userPrivilegeIntersect(Set<String> requiredPrivileges);
}
