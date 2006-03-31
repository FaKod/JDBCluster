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
package org.jdbcluster.metapersistence.cluster;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.exception.ClusterTypeException;

/**
 * 
 * @author Philipp Noggler
 * class ClusterFactory is responsible for creating instances
 * of type Cluster
 *
 */
public class ClusterFactory {

	/**
	 * creates an instance of a Cluster
	 * @param ct specifies the ClusterType class that should be returned
	 * @return Cluster
	 */
	public static <T extends Cluster> T newInstance(ClusterType ct) {
		String className = ClusterTypeBase.getClusterTypeConfig().getClusterClassName(ct.getName());
		if (className == null) {
			return (T) new ClusterImpl(); //if no classname was defined, return empty object
		}

		Class<?> clusterClass;
		Cluster cluster = null;
		try {
			//create a new instance with given classname
			clusterClass = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			cluster = (Cluster) clusterClass.newInstance();
		} catch (InstantiationException e) {
			throw new ClusterTypeException("specified class [" + className + "] object cannot be instantiated because it is an interface or is an abstract class", e);
		} catch (IllegalAccessException e) {
			throw new ClusterTypeException("the currently executed ctor for class [" + className + "] does not have access", e);
		} catch (ClassNotFoundException e) {
			throw new ClusterTypeException("no definition for the class [" + className + "] with the specified name could be found", e);
		}
		return (T) cluster;
	}
	
}
