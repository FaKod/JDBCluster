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

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.jdbcluster.JDBClusterConfigurationBase;

/**
 * class PrivilegeConfigImpl is an implementation of PrivilegeConfig.
 * It saves the location of the configuration file and
 * exexutes some queries on that XML file.
 * @author FaKod
 *
 */
public class PrivilegeConfigImpl extends JDBClusterConfigurationBase implements PrivilegeConfig {
	
	/**
	 * Default constructor
	 *
	 */
	public PrivilegeConfigImpl() {}
	
	/**
	 * Constructor which sets the config path
	 * @param config the configuration to be set
	 */
	public PrivilegeConfigImpl(String config) {
		this.setConfiguration(config);
	}

	/**
	 * returns the config path
	 * @return String
	 */
	public String getConfiguration() {
		return configuration;
	}
	
	/**
	 * sets the path, reads out the document and saves it
	 * @param configuration the config path
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
		try {
			document = read(configuration);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	public String getUserPrivilegeCheckerPath() {
		
		String xPath = "//jdbcluster/privileges/UserPrivilegeChecker";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.valueOf("@class");
		}
	}
}
