package org.jdbcluster.logging;

import org.aspectj.lang.JoinPoint;
import org.jdbcluster.tracing.TracingAspect;

public abstract aspect LoggingAspect extends TracingAspect {
	
	protected abstract pointcut logExceptionThrow( );
	
	protected abstract pointcut exceptionsToBeLogged( );
	
	private pointcut filteredExceptionCapture( ) :
				exceptionsToBeLogged( ) &&
				!pointsToBeExcluded( );
	
	before( ) : filteredExceptionCapture( )
	{
		logException(thisJoinPoint);
	}
	
	after( ) throwing: logExceptionThrow( )
	{
		logExceptionThrow(thisJoinPoint);
	}
	
	protected abstract void logException(JoinPoint joinPoint);
	protected abstract void logExceptionThrow(JoinPoint joinPoint);
}
