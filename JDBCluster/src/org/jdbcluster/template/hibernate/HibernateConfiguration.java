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
package org.jdbcluster.template.hibernate;

import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.template.ConfigurationTemplate;
import org.jdbcluster.template.SessionFactoryTemplate;
import org.springframework.util.Assert;

/**
 * HBMConfiguration is an implementation of ConfigurationTemplate
 * and provides Hibernate specific information about sessions, sessionfactory
 * and Hibernate configuration (e.g. config file)
 * 
 * @author Philipp Noggler
 *
 */
public class HibernateConfiguration implements ConfigurationTemplate {

	/*
	 * Hibernate configuration
	 */
	protected Configuration cfg = null;

	/*
	 * Session Factory
	 */
	protected SessionFactory hibernateFactory;
	
	
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
	
	/**
	 * @return SessionFactoryTemplate
	 */
	public SessionFactoryTemplate buildSessionFactory() {
		hibernateFactory = cfg.buildSessionFactory();
		return new HibernateSessionFactory(hibernateFactory);
	}

	/**
	 * returns the native configuration object
	 * @return T
	 */
	@SuppressWarnings("unchecked")
	public <T> T getNativeConfiguration() {
		return (T) cfg;
	}

	/**
	 * calculated the max length of an attribute of type String
	 * @param c Cluster that holds the attribute
	 * @param attributeName name of the attribute
	 * @return max allowed length of the attribute
	 */
	public int getLenthOfStringAttribute(Cluster c, String attributeName) {
		
		Assert.notNull(c, "c may not be null");
		Assert.notNull(attributeName, "attributeName may not be null");
		
		PersistentClass pc = cfg.getClassMapping(c.getDaoClass().getName());
		
		if(pc==null)
			throw new ConfigurationException("Persistent Dao Class from Cluster " + c.getClass().getName() + " is not Hibernate configured");
		
    Property p = pc.getProperty(attributeName);
    
    if(p==null)
    	throw new ConfigurationException("Property " + attributeName + " in cluster " + c.getClass().getName() + " not found");
    
    Iterator<Column> i = (Iterator<Column>) p.getValue().getColumnIterator();
    Column column = i.next();
    return column.getLength();
	}

}
