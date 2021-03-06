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

import java.io.Serializable;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.ICluster;
import org.jdbcluster.metapersistence.security.user.IUser;

/**
 * 
 * @author FaKod
 * @author Philipp Noggler
 */
public interface SessionTemplate {

	public TransactionTemplate beginTransaction();
	public void cancelQuery();
	public QueryTemplate createQuery(String queryString);
	/**
	 * creates a query with given parameters and saves the query object
	 * (to get the result later on)
	 * @param ccf the filter that contains the criteria for the select statement
	 * @return QueryTemplate the query object
	 */
	public QueryTemplate createQuery(CCFilter ccf);
	public QueryTemplate getNamedQuery(String queryName);
	public void delete(ICluster cluster);
	public void saveOrUpdate(ICluster cluster);
	public void merge(ICluster cluster);
	public void save(ICluster cluster);
	public void close();
	public void update(ICluster cluster);
	public void save(String id, ICluster cluster);
	
	public ICluster load(Class<? extends ICluster> clusterClass, Serializable id);
	public ICluster load(ClusterType clusterType, Serializable id);
	public ICluster load(String clusterTypeName, Serializable id);
	public ICluster load(ICluster cluster, Serializable id);
	
	public ICluster get(Class<? extends ICluster> clusterClass, Serializable id);
	public ICluster get(ClusterType clusterType, Serializable id);
	public ICluster get(String clusterTypeName, Serializable id);
	public ICluster get(ICluster cluster, Serializable id);
	
	public void refresh(ICluster cluster);
	public void evict(ICluster cluster);
	public void persist(ICluster cluster);
	public <T> T getNativeSession();
	public TransactionTemplate getTransactionTemplate();
	public boolean isDirty();
	/**
	 * determines whether the given cluster object is in the session or not.
	 * @param cluster which is in session or not.
	 * @return true if the given cluster object is in the session, false otherwise.
	 */
	public boolean contains(ICluster cluster);
	
	/**
	 * activates the given Filter on the Session. The Filter contains the name, parameter name 
	 * @param sessionFilter the filter with the given name will be activated on the session.
	 */
	public void enableFilter(SessionFilter sessionFilter);
	
	public IUser getUser();
}
