package org.jdbcluster.template.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.DBException;
import org.jdbcluster.exception.JDBClusterException;
import org.jdbcluster.template.QueryTemplate;

/**
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt 
 * 	HBMQuery is an implementation of QueryTemplate. It
 *  provides information about the query and is responsible for executing
 *  a hibernate specific query.
 * 
 */
public class HibernateQuery implements QueryTemplate {

	// query object which contains information about the result after execution
	private Query query;
	@SuppressWarnings("unused")
	private ClusterType clusterType;

	public HibernateQuery() {
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
	public List list()throws JDBClusterException {
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
