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
package org.jdbcluster.clustercontainer;

import org.jdbcluster.clustercontainer.identifier.Identifier;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.template.SessionTemplate;

/**
 * central container for cluster objects
 * holds cluster as a resultset defined through CCFiler
 * keeps objects actual through receiving notification
 * from Notification Server
 * Notifies all interested objects
 * @author Christopher Schmidt
 *
 */
public interface ClusterContainer {

	/**
	 * returns actual stored session
	 * @return actual stored session
	 */
	public SessionTemplate getSession();

	/**
	 * sets actual sessionTemplate to use
	 * @param session session to use
	 */
	public void setSession(SessionTemplate session);

	/**
	 * removes all elements from container and queries for
	 * the through CCFilter defines resultset
	 * @param iccf
	 */
	public void fill(CCFilter iccf);

	/**
	 * fills container with all available ClusterType objects
	 *
	 */
	public void fill();

	/**
	 * database updated object identified through id
	 * @param id primary key of Cluster object
	 */
	public void dBUpdate(Identifier id);
	
	/**
	 * database updated object identified through index
	 * @param index index of object
	 */
	public void dBUpdateAt(int index);

	/**
	 * delete object identified through id
	 * @param id primary key of Cluster object
	 */
	public void dBDelete(Identifier id);
	
	/**
	 * delete object identified through index in cluster container
	 * @param index index of object
	 */
	public void dBDeleteAt(int index);

	/**
	 * inserts new Cluster Object. There is no check if this Cluster 
	 * Object fits into the given resultset!
	 * @param cluster cluster object to insert
	 */
	public void dBInsert(ICluster cluster);

	/**
	 * inserts new Cluster Object. There is no check if this Cluster 
	 * Object fits into the given resultset!
	 * @param cluster cluster object to insert
	 * @param index insert at index position
	 */
	public void dBInsertAt(ICluster cluster, int index);

	/**
	 * retreive cluster from ClusterContainer. If the object is not
	 * already loaded from DB it's loaded.
	 * @param id Identifier or primary key of Cluster Object
	 * @return Cluster Object
	 */
	public ICluster getCluster(Identifier id);

	/**
	 * retreive cluster from ClusterContainer. If the object is not
	 * already loaded from DB it's loaded.
	 * @param index index of ClusterObject inside cluster container
	 * @return Cluster Object
	 */
	public ICluster getClusterAt(int index);

	/**
	 * searches for a Cluster Object with the given 
	 * Identifier / primary key
	 * @param id Identifier / primary key of Cluster Object
	 * @return index
	 */
	public int indexOf(Identifier id);
	
	/**
	 * searches for index with the given 
	 * Cluster Object
	 * @param cluster Cluster Object
	 * @return index
	 */
	public int indexOf(ICluster cluster);

	/**
	 * returns amount of elements in Cluster Container
	 * @return size
	 */
	public int size();

	/**
	 * actual CCFilter
	 * @return instance of current CCFilter
	 */
	public CCFilter getICCFilter();

	/**
	 * which cluster type this instance of CC is holding
	 * @return ClusterType
	 */
	public ClusterType getClusterType();

	/**
	 * add a Cluster Notification Listener to receive notification
	 * on a change of a Cluster Object
	 * @param l ClusterNotificationListener
	 */
	public void addClusterNotificationListener(ClusterNotificationListener l);

	/**
	 * remove a Cluster Notification Listener
	 * @param l ClusterNotificationListener
	 */
	public void removeClusterNotificationListener(ClusterNotificationListener l);
}
