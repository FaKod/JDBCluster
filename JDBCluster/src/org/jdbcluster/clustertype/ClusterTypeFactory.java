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

import org.jdbcluster.exception.ClusterTypeException;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.springframework.util.Assert;

/**
 * 
 * @author Philipp Noggler
 * class ClusterTypeFactory is responsible for creating
 * ClusterType objects
 * @param <T>
 */
public abstract class ClusterTypeFactory extends ClusterTypeBase{

	/**
	 * creates ClusterType objects depending on the ClusterType name which is passed as
	 * an argument
	 * @param <T> is a template which extends the interface ClusterType
	 * @param clusterTypeName the ClusterType's name as a String
	 * @return clusterType the ClusterType object
	 * @throws ClusterTypeException
	 */
	@SuppressWarnings("unchecked")
	static public <T extends ClusterType> T newInstance(String clusterTypeName) throws ClusterTypeException {
		
		Assert.notNull(clusterTypeName, "clusterTypeName  may not be null");
		Assert.hasLength(clusterTypeName,"clusterTypeName  may not have zero length");
		
		//get the classname of the object
		String className = ClusterTypeBase.getClusterTypeConfig().getClusterClassName(clusterTypeName);
		//if no name was defined, throw an exception
		if (className == null) {
			throw new ClusterTypeException("No ClusterType of type " + clusterTypeName + " found!", new Throwable());
		}
		
		ClusterTypeBase clusterType = new ClusterTypeImpl();

		//save the name
		clusterType.setName(clusterTypeName);
		return (T) clusterType;
	}
	
	static public <T extends ClusterType> T newInstance(Class<? extends ICluster> clazz) throws ClusterTypeException {
		
		Assert.notNull(clazz, "Class  may not be null");
		
		String ct = ClusterTypeBase.getClusterTypeConfig().getClusterTypeFromClass(clazz.getName());
		return newInstance(ct);
	}
}