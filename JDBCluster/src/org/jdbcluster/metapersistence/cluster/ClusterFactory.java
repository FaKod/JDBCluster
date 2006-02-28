package org.jdbcluster.metapersistence.cluster;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.exception.ClusterTypeException;

/**
 * 
 * @author Philipp Noggler
 * class ClusterFactory is responsible for creating instances
 * of type Cluster
 *
 */
public class ClusterFactory {

	/**
	 * creates an instance of a Cluster
	 * @param ct specifies the ClusterType class that should be returned
	 * @return Cluster
	 */
	public static <T extends Cluster> T newInstance(ClusterType ct) {
		String className = ClusterTypeBase.getClusterTypeConfig().getClusterClassName(ct.getName());
		if (className == null) {
			return (T) new ClusterImpl(); //if no classname was defined, return empty object
		}

		Class<?> clusterClass;
		Cluster cluster = null;
		try {
			//create a new instance with given classname
			clusterClass = Class.forName(className, false, Thread.currentThread().getContextClassLoader());
			cluster = (Cluster) clusterClass.newInstance();
		} catch (InstantiationException e) {
			throw new ClusterTypeException("specified class [" + className + "] object cannot be instantiated because it is an interface or is an abstract class", e);
		} catch (IllegalAccessException e) {
			throw new ClusterTypeException("the currently executed ctor for class [" + className + "] does not have access", e);
		} catch (ClassNotFoundException e) {
			throw new ClusterTypeException("no definition for the class [" + className + "] with the specified name could be found", e);
		}
		return (T) cluster;
	}
	
}
