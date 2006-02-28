package org.jdbcluster.template;

import java.util.List;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.JDBClusterException;


/**
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 * QueryTemplate is an interface which provides methods to execute a Query.
 *
 */
public interface QueryTemplate {
	
	/**
	 * Return the query results as a <tt>List</tt>. If the query contains
	 * multiple results pre row, the results are returned in an instance
	 * of <tt>Object[]</tt>.
	 *
	 * @return the result list
	 * @throws JDBClusterException
	 */
	public List list() throws JDBClusterException;
	
	/**
	 * stores instance of corresponding ClusterType
	 * @param clusterType
	 */
	public void setClusterType(ClusterType clusterType);
}
