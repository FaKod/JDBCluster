package org.jdbcluster.filter;

import java.util.HashMap;

import org.jdbcluster.clustertype.ClusterType;

/**
 * 
 * @author Philipp Noggler
 * interface CCFilter: All filters are implementing this
 * interface
 * 
 */

public interface CCFilter {

	/**
	 * calls getWhere in ClusterSelectImpl and returns a String
	 * to generate a query
	 * @param selectID specifies the select ID
	 */
	public String getSelectString(String SelectID);
	
	/**
	 * appends a Filter to another filter
	 * @param CCFilter the Filter to be appended
	 */
	public void append(CCFilter filter);
	
	/**
	 * returns a HashMap which contains the Binding. The key (var) is 
	 * mapped to a value (attribute)
	 * @param ct specifies the ClusterType
	 * @param selID specifies the select ID
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID);
	
	
	/**
	 * returns the wherestatement as a String
	 * @return String
	 */
	public String getWhereStatement();
	
	/**
	 * returns the selectstatement (without the where clause) of a filter
	 * @return selectStatement the select statement
	 */
	public String getSelectStatementDAO();
	
	/**
	 * returns the classname of a filter as a String
	 * @return className the classname of that filter
	 */
	public String getClassName();
	
	/**
	 * returns the binding mapped in a HashMap
	 * @return HashMap
	 */
	public HashMap<String, String> getBinding();

	/**
	 * returns the filter which is appended to the root filter
	 * @return CCFilter the appended filter
	 */
	public CCFilter getAppendedFilter();
	
	/**
	 * returns the clustertype of a filter
	 * @return ClusterType
	 */
	public ClusterType getClusterType();
	
}