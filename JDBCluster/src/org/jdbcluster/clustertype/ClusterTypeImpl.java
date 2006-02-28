package org.jdbcluster.clustertype;


/**
 * 
 * @author Philipp Noggler
 * class ClusterTypeImpl forwards the call to ClusterTypeConfigImpl
 * and returns the result as a string. The Constructor just saves 
 * the name (e.g. unit), that was returned by getClusterType() into the super.name
 * variable
 * 
 */
public class ClusterTypeImpl extends ClusterTypeBase implements ClusterType {

	/**
	 * Default Constructor
	 *
	 */
	public ClusterTypeImpl() {}
	
	/**
	 * Constructor which automatically saves the name
	 * of the passed ClusterType name
	 * @param clusterTypeName
	 */
	public ClusterTypeImpl(String clusterTypeName) {
		setName(getClusterType(clusterTypeName));
	}
	
	/**
	 * gets a specific ClusterType by given CTName
	 * @param clusterTypeName specifies the ClusterType's name
	 * @return String
	 */
	public String getClusterType(String clusterTypeName) {
		return getClusterTypeConfig().getClusterType(clusterTypeName);
	}

	public String getClusterClassName() {
		return getClusterTypeConfig().getClusterClassName(getName());
	}

}
