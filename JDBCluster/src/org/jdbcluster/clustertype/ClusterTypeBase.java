package org.jdbcluster.clustertype;

/**
 * 
 * @author Philipp Noggler
 * abstract ClusterTypeBase which keeps the name of a specific
 * clustertype e.g. unit and the config file, where all of the
 * clustertypes are defined
 */

public abstract class ClusterTypeBase {

	//reference to a ClusterTypeConfig object
	static private ClusterTypeConfig cTypeConfig;
	private String name;
	private String clusterClassName;

	/**
	 * gets the ClusterTypeConfig object which contains information
	 * about "clustertype.xml"
	 * @return ClusterTypeConfig
	 */
	public static ClusterTypeConfig getClusterTypeConfig() {
		return cTypeConfig;
	}

	/**
	 * sets the ClusterTypeConfig object which contains information
	 * about "clustertype.xml"
	 * @param type object to be set
	 */
	public static void setClusterTypeConfig(ClusterTypeConfig type) {
		cTypeConfig = type;
	}
	
	/**
	 * gets the ClusterType name
	 * @return String
	 */
	public String getName() {
		return name;
	}

	/**
	 * sets the ClusterType's name
	 * @param name value to be set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
