package org.jdbcluster.clustertype;

/**
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 * interface ClusterTypeConfig is defining methods to execute
 * queries on XML file.
 */

public interface ClusterTypeConfig {

	/**
	 * returns the name of the specified clustertype 
	 * or null if there is none (via xPath)
	 * @param clusterTypeName ClusterType name
	 * @return String
	 */
	public String getClusterType(String clusterTypeName);

	/**
	 * returns the configures Cluster Class name
	 * @param clusterTypeName cluster type name in config file
	 * @return cluster class name as string
	 */
	public String getClusterClassName(String clusterTypeName);
	
}
