package org.jdbcluster.clustertype;

/**
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 * interface ClusterType defines methods to get a specific 
 * ClusterType and it's name.
 */

public interface ClusterType {
	
	/**
	 * gets a specific ClusterType by given CTName
	 * @param CTName specifies the ClusterType's name
	 * @return String
	 */
	public String getClusterType(String CTName);
	
	/**
	 * gets the ClusterType name
	 * @return String
	 */
	public String getName();
	
	/**
	 * gets the configured Cluster Class Name as a String
	 * @return Class Name of Cluster Object
	 */
	public String getClusterClassName();
}
