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
