package mycluster.privilege;

import java.util.Set;

import org.jdbcluster.privilege.UserPrivilegeChecker;

public class UserPrivilege implements UserPrivilegeChecker {

	public boolean userPrivilegeIntersect(Set<String> requiredPrivileges) {
		return true;
	}

}
