package org.jdbcluster.filter;

import java.util.HashMap;

import org.jdbcluster.clustertype.ClusterType;

/**
 * 
 * @author Philipp Noggler
 * interface ClusterSelect defines methods to create a query
 *
 */

public interface ClusterSelect {
	
	/**
	* creates a select string on "selects.xml"
	* @param clusterType identifies the ClusterType
	* @param SelectID selects the SelectID
	* @return String
	*/
	public String getWhere(ClusterType clusterType, String SelectID);
	
	/**
	 * gets the classname from "selects.xml"
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getClassName(ClusterType clusterType, String SelectID);
	
	/**
	 * gets the bindings for the specified classname
	 * @param ct identifies the ClusterType
	 * @param selID selects the SelectID
	 * @param className specifies the classname
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID, String className);
}
