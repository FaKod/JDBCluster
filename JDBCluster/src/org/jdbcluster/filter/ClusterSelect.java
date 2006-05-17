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
package org.jdbcluster.filter;

import java.util.HashMap;

import org.jdbcluster.clustertype.ClusterType;

/**
 * interface ClusterSelect defines methods to create a query
 * 
 * @author Philipp Noggler
 *
 */

public interface ClusterSelect {
	
	/**
	* creates a select string on "selects.xml"
	* @param clusterType identifies the ClusterType
	* @param SelectID selects the SelectID
	* @return String
	*/
	public String getWhere(ClusterType clusterType, String SelectID);
	
	/**
	 * returns the alias setting
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getAlias(ClusterType clusterType, String SelectID);
	
	/**
	 * gets the classname from "selects.xml"
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getClassName(ClusterType clusterType, String SelectID);
	
	/**
	 * gets the bindings for the specified classname
	 * @param ct identifies the ClusterType
	 * @param selID selects the SelectID
	 * @param className specifies the classname
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID, String className);

	/**
	 * gets query extension string
	 * @param clusterType identifies the ClusterType
	 * @param selId selects the SelectID
	 * @return String
	 */
	public String getExt(ClusterType clusterType, String selId);
}
