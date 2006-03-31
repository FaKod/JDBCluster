package org.jdbcluster.privilege;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.exception.ConfigurationException;

/**
 * Base class for handling privileges
 * @author FaKod
 *
 */
public abstract class PrivilegeBase {
	
	/**
	 * stores implementation instance of PrivilegeConfig 
	 */
	static private PrivilegeConfig privilegeConfig;
	
	static private UserPrivilegeChecker userPrivilege;

	/**
	 * get the configured instance of UserPrivilege
	 * @return instance of Interface UserPrivilegeChecker
	 */
	public static UserPrivilegeChecker getUserPrivilege() {
		if(privilegeConfig==null)
			throw new ConfigurationException("missing configuration for privileges");
		
		String path = privilegeConfig.getUserPrivilegeCheckerPath();
		
		if(path == null || path.length()==0)
			throw new ConfigurationException("missing configuration tag <UserPrivilegeChecker...> for privileges.");
		
		if(userPrivilege==null) {
			userPrivilege = (UserPrivilegeChecker)JDBClusterUtil.createClassObject(privilegeConfig.getUserPrivilegeCheckerPath());
		}
		return userPrivilege;
	}

	/**
	 * return configuration instance
	 * @return implementation instance of PrivilegeConfig 
	 */
	public static PrivilegeConfig getPrivilegeConfig() {
		return privilegeConfig;
	}

	/**
	 * sets configuration instance
	 * @param privilegeConfig implementation instance
	 */
	public static void setPrivilegeConfig(PrivilegeConfig privilegeConfig) {
		PrivilegeBase.privilegeConfig = privilegeConfig;
	}

}
