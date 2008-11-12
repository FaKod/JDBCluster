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

/**
 * class that intercepts cluster operations
 * @see tag <clustertype clusterInterceptor="...">
 * @author FaKod
 *
 */
public interface ClusterInterceptor {
	
	/**
	 * calles after cluster creation
	 * clusterNew is not called if cluster is created from a persisten object
	 * @param cluster created cluster object
	 * @return boolean true if successful
	 */
	public boolean clusterNew(ICluster cluster);
	
	/**
	 * called after a cluster is reloaded from database
	 * used in session methods
	 * @param cluster refreshed cluster
	 * @return boolean reserved return true
	 */
	public boolean clusterRefresh(ICluster cluster);

}
