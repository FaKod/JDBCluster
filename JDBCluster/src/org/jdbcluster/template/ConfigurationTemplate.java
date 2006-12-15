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
package org.jdbcluster.template;

import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * 
 * @author Philipp Noggler
 * @author FaKod
 *
 */
public interface ConfigurationTemplate {

	/**
	 * Set the Configuration
	 * @param cfg the Configuration Object
	 */
	public void setConfiguration(Object cfg);
	
	/**
	 * creates a session factory
	 * @return SessionFactoryTemplate
	 */
	public SessionFactoryTemplate buildSessionFactory();
	
	/**
	 * calculated the max length of an attribute of type String
	 * @param c Cluster that holds the attribute
	 * @param attributeName name of the attribute
	 * @return max allowed length of the attribute
	 */
	public int getLenthOfStringAttribute(Cluster c, String attributeName);
	
	/**
	 * returns the native configuration object
	 * @return T
	 */
	public <T> T getNativeConfiguration();
}
