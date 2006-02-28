package org.jdbcluster.metapersistence.cluster;

public aspect ClusterUsage {
	pointcut newCluster(): 
		call(Cluster+.new(..)) && 
		!withincode(* ClusterFactory.*(..));

	declare error: newCluster(): 
		"please use static factory method ClusterFactory.newInstance(ClusterType)";
}
