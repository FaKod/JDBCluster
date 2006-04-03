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
 * 
 * @author Philipp Noggler
 * class ClusterTypeImpl forwards the call to ClusterTypeConfigImpl
 * and returns the result as a string. The Constructor just saves 
 * the name (e.g. unit), that was returned by getClusterType() into the super.name
 * variable
 * 
 */
public class ClusterTypeImpl extends ClusterTypeBase implements ClusterType {

	/**
	 * Default Constructor
	 *
	 */
	public ClusterTypeImpl() {}
	
	/**
	 * Constructor which automatically saves the name
	 * of the passed ClusterType name
	 * @param clusterTypeName
	 */
	public ClusterTypeImpl(String clusterTypeName) {
		setName(getClusterType(clusterTypeName));
	}
	
	/**
	 * gets a specific ClusterType by given CTName
	 * @param clusterTypeName specifies the ClusterType's name
	 * @return String
	 */
	public String getClusterType(String clusterTypeName) {
		return getClusterTypeConfig().getClusterType(clusterTypeName);
	}

	public String getClusterClassName() {
		return getClusterTypeConfig().getClusterClassName(getName());
	}

}
