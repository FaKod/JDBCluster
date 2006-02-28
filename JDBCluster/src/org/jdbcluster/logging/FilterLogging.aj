package org.jdbcluster.logging;

import org.apache.log4j.Logger;

public aspect FilterLogging extends JDBClusterLogging {

	static Logger logger = Logger.getLogger(FilterLogging.class);

	protected Logger getLogger() {
		return logger;
	}
	
	public pointcut pointsToBeTraced( ) : 
		call(* org.jdbcluster.filter.*.*(..));

	public pointcut pointsToBeExcluded( ) : 
		call(* java.*.*(..));
}
