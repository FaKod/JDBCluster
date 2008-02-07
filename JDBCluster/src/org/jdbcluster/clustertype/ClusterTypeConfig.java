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

import java.util.List;

/**
 * interface ClusterTypeConfig is defining methods to execute
 * queries on XML file.
 * @author Philipp Noggler
 * @author Christopher Schmidt
 * @author Tobi
 */

public interface ClusterTypeConfig {

	/**
	 * returns the name of the specified clustertype 
	 * or null if there is none (via xPath)
	 * @param clusterTypeName ClusterType name
	 * @return String
	 */
	public String getClusterType(String clusterTypeName);

	/**
	 * returns the configures Cluster Class name
	 * @param clusterTypeName cluster type name in config file
	 * @return cluster class name as string
	 */
	public String getClusterClassName(String clusterTypeName);
	
	/**
	 * returns a list of all cluster ids
	 * @return List<String>
	 */
	public List<String> getClusterIDs();
	
	/**
	 * class name of cluster interceptor
	 * @return String
	 */
	public String getClusterInterceptorClassName();
	
}
