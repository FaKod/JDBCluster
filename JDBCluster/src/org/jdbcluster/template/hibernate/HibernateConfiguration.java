package org.jdbcluster.template.hibernate;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jdbcluster.template.ConfigurationTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;

/**
 * 
 * @author Philipp Noggler
 * HBMConfiguration is an implementation of ConfigurationTemplate
 * and provides Hibernate specific information about sessions, sessionfactory
 * and Hibernate configuration (e.g. config file)
 *
 */
public class HibernateConfiguration implements ConfigurationTemplate {

	//Hibernate configuration
	private Configuration cfg = null;

	private SessionFactory hibernateFactory;
	
	
	/**
	 * getter of configuration
	 * @return
	 */
	public Configuration getConfiguration() {
		return cfg;
	}

	/**
	 * setter of configuration
	 * @param config
	 */
	public void setConfiguration(Object config) {
		cfg = new Configuration().configure((String)config);	
	}
	
	public SessionFactoryTemplate buildSessionFactory() {
		hibernateFactory = cfg.buildSessionFactory();
		return new HibernateSessionFactory(hibernateFactory);
	}

}
