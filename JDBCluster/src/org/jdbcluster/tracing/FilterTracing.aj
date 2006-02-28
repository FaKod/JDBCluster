package org.jdbcluster.tracing;

import org.jdbcluster.tracing.TracingAspect;

public aspect FilterTracing extends TracingAspect{

	public pointcut pointsToBeTraced( ) : call(* org.jdbcluster.filter.*.*(..));
	public pointcut pointsToBeExcluded( ) : call(void java.io.PrintStream.*(..));
}
