package org.jdbcluster.clustertype;

public aspect ClusterTypeUsage {
	pointcut newClusterType(): 
		call(ClusterType+.new(..)) && 
		!withincode(* ClusterTypeFactory.*(..));

	declare error: newClusterType(): 
		"please use static factory method ClusterTypeFactory.newInstance()";
}
