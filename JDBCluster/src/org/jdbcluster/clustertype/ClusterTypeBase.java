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
package org.jdbcluster.clustertype;

/**
 * abstract ClusterTypeBase which keeps the name of a specific
 * clustertype e.g. unit and the config file, where all of the
 * clustertypes are defined
 * @author Philipp Noggler
 */

public abstract class ClusterTypeBase {

	//reference to a ClusterTypeConfig object
	static private ClusterTypeConfig cTypeConfig;
	private String name;
	private String clusterClassName;

	/**
	 * gets the ClusterTypeConfig object which contains information
	 * about "clustertype.xml"
	 * @return ClusterTypeConfig
	 */
	public static ClusterTypeConfig getClusterTypeConfig() {
		return cTypeConfig;
	}

	/**
	 * sets the ClusterTypeConfig object which contains information
	 * about "clustertype.xml"
	 * @param type object to be set
	 */
	public static void setClusterTypeConfig(ClusterTypeConfig type) {
		cTypeConfig = type;
	}
	
	/**
	 * gets the ClusterType name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the ClusterType's name
	 * @param name value to be set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
