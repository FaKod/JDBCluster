package org.jdbcluster.clustertype;

import org.jdbcluster.exception.ClusterTypeException;

/**
 * 
 * @author Philipp Noggler
 * class ClusterTypeFactory is responsible for creating
 * ClusterType objects
 * @param <T>
 */
public class ClusterTypeFactory extends ClusterTypeBase{

	/**
	 * creates ClusterType objects depending on the ClusterType name which is passed as
	 * an argument
	 * @param <T> is a template which extends the interface ClusterType
	 * @param clusterTypeName the ClusterType's name as a String
	 * @return clusterType the ClusterType object
	 * @throws ClusterTypeException
	 */
	@SuppressWarnings("unchecked")
	static public <T extends ClusterType> T newInstance(String clusterTypeName) throws ClusterTypeException {
		//get the classname of the object
		String className = ClusterTypeBase.getClusterTypeConfig().getClusterClassName(clusterTypeName);
		//if no name was defined, throw an exception
		if (className == null) {
			throw new ClusterTypeException("No ClusterType of type " + clusterTypeName + "found!", new Throwable());
		}
		
		ClusterTypeBase clusterType = new ClusterTypeImpl();

		//save the name
		clusterType.setName(clusterTypeName);
		return (T) clusterType;
	}
}