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

import org.jdbcluster.filter.CCFilter;
import org.jdbcluster.metapersistence.cluster.Cluster;

public interface StatelessSessionTemplate {

	public TransactionTemplate beginTransaction();
	public QueryTemplate createQuery(String queryString);
	/**
	 * creates a query with given parameters and saves the query object
	 * (to get the result later on)
	 * @param ccf the filter that contains the criteria for the select statement
	 * @return QueryTemplate the query object
	 */
	public QueryTemplate createQuery(CCFilter ccf);
	public QueryTemplate getNamedQuery(String queryName);
	public void delete(Cluster cluster);
	public void close();
	public void update(Cluster cluster);
	public Cluster get(Class<? extends Cluster> clusterClass, Serializable id);
	public Cluster get(Cluster cluster, Serializable id);
	public void refresh(Cluster cluster);
	public <T> T getNativeSession();
	public TransactionTemplate getTransactionTemplate();
	
}
