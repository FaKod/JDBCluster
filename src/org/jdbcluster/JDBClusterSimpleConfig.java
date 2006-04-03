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
package org.jdbcluster;

import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.clustertype.ClusterTypeConfigImpl;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.dao.DaoConfigImpl;
import org.jdbcluster.domain.DomainBase;
import org.jdbcluster.domain.DomainConfigImpl;
import org.jdbcluster.filter.CCFilterBase;
import org.jdbcluster.filter.ClusterSelectImpl;
import org.jdbcluster.privilege.PrivilegeBase;
import org.jdbcluster.privilege.PrivilegeConfigImpl;
import org.jdbcluster.template.ConfigurationFactory;

/**
 * simple configuration helper class
 * @author Christopher Schmidt
 *
 */
public abstract class JDBClusterSimpleConfig {
	
	/**
	 * sets configuration for HIBERNATE config file and
	 * HIBERNATE internal template
	 * @param templatePath if null then use internal template
	 * @param hibernateConfig path to hibernate config file
	 */
	static public void setHibernateConfiguration(String templatePath, String hibernateConfig) {
		//configuring template implementation to use
		//HIBERNATE in this case
		if(templatePath==null)
			ConfigurationFactory.setConfigurationClass("org.jdbcluster.template.hibernate.HibernateConfiguration");
		else
			ConfigurationFactory.setConfigurationClass(templatePath);
		//and HIBERNATE need its config file too
		ConfigurationFactory.getInstance().setConfiguration(hibernateConfig);
	}
	
	static public void setConfiguration(String pathToJDBClusterConfiguration) {
		// available cluster types and theire classes
		ClusterTypeBase.setClusterTypeConfig(new ClusterTypeConfigImpl(pathToJDBClusterConfiguration));
		// sets Dao Configuration file
		Dao.setDaoConfig(new DaoConfigImpl(pathToJDBClusterConfiguration));
		// available filer classes
		CCFilterBase.setFilterConfig(new ClusterSelectImpl(pathToJDBClusterConfiguration));
		// available Domain Validation
		DomainBase.setDomainConfig(new DomainConfigImpl(pathToJDBClusterConfiguration));
		// available privilege configuration
		PrivilegeBase.setPrivilegeConfig(new PrivilegeConfigImpl(pathToJDBClusterConfiguration));
	}

}
