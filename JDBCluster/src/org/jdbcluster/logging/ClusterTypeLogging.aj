package org.jdbcluster.logging;

import org.apache.log4j.Logger;

public aspect ClusterTypeLogging extends JDBClusterLogging {

	static Logger logger = Logger.getLogger(ClusterTypeLogging.class);

	protected Logger getLogger() {
		return logger;
	}
	
	public pointcut pointsToBeTraced( ) : 
		call(* org.jdbcluster.clustertype.*.*(..));

	public pointcut pointsToBeExcluded( ) : 
		call(* java.*.*(..));
}
