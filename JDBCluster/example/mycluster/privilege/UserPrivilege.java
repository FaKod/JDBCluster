package mycluster.privilege;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.jdbcluster.privilege.UserPrivilegeChecker;

public class UserPrivilege implements UserPrivilegeChecker {
	
	/** Logger available to subclasses */
	protected final static Logger logger = Logger.getLogger(UserPrivilege.class);
	
	/**
	 * property for test case
	 */
	private static HashSet<String> privSet = new HashSet<String>();

	/**
	 * accessors for test case
	 */
	public static Set<String> getPrivSet() {
		return privSet;
	}
	
	/**
	 * accessors for test case
	 */
	public static void clearLastprivSet() {
		logger.info("Priv list cleared");
		privSet.clear();
	}

	public boolean userPrivilegeIntersect(Set<String> requiredPrivileges) {
		logger.info("needed privileges: " + requiredPrivileges);
		
		privSet.addAll(requiredPrivileges);
		
		return true;
	}
	
	
}
