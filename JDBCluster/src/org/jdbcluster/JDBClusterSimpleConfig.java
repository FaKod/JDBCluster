package org.jdbcluster;

import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.clustertype.ClusterTypeConfigImpl;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.dao.DaoConfigImpl;
import org.jdbcluster.filter.CCFilterBase;
import org.jdbcluster.filter.ClusterSelectImpl;
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
	}

}
