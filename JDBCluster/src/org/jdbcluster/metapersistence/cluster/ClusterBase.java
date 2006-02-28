package org.jdbcluster.metapersistence.cluster;

import org.jdbcluster.clustertype.ClusterType;

public abstract class ClusterBase {

	private ClusterType clusterType;
	
	public void setClusterType(ClusterType ct) {
		clusterType = ct;
	}
	
	public ClusterType getClusterType() {
		return clusterType;
	}
}
