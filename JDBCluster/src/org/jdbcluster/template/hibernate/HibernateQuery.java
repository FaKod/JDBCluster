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
package org.jdbcluster.template.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.DBException;
import org.jdbcluster.exception.JDBClusterException;
import org.jdbcluster.template.QueryTemplate;

/**
 * HBMQuery is an implementation of QueryTemplate. It
 * provides information about the query and is responsible for executing
 * a hibernate specific query.
 * @author Philipp Noggler
 * @author Christopher Schmidt 
 * 	
 */
public class HibernateQuery implements QueryTemplate {

	// query object which contains information about the result after execution
	private Query query;

	private ClusterType clusterType;

	public HibernateQuery() {
	}
	
	public HibernateQuery(Query query) {
		setQuery(query);
	}


	/**
	 * getter of querystring
	 * 
	 * @return query
	 */
	public Query getQuery() {
		return query;
	}

	/**
	 * setter of querystring
	 * 
	 * @param query
	 */
	public void setQuery(Query query) {
		this.query = query;
	}

	/**
	 * Return the query results as a <tt>List</tt>. If the query contains
	 * multiple results pre row, the results are returned in an instance
	 * of <tt>Object[]</tt>.
	 *
	 * @return the result list
	 * @throws JDBClusterException
	 */
	@SuppressWarnings("unchecked")
	public List list()throws JDBClusterException {
		try {
			return query.list();
		} catch (HibernateException e) {
			throw new DBException("underlaying Hibernate exception", e);
		}
	}
	
	/**
	 * Return the query results as a <tt>List</tt>. If the query contains
	 * multiple results pre row, the results are returned in an instance
	 * of <tt>Object[]</tt>.
	 *
	 * @return the result list
	 * @throws JDBClusterException
	 */
	@SuppressWarnings("unchecked")
	public List listUnique()throws JDBClusterException {
		try {
			return query.list();
		} catch (HibernateException e) {
			throw new DBException("underlaying Hibernate exception", e);
		}
	}

	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}


	public ClusterType getClusterType() {
		return clusterType;
	}

}
