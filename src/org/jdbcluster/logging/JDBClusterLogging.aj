/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.logging;

import org.jdbcluster.logging.LoggingAspect;
import org.aspectj.lang.JoinPoint;
import org.jdbcluster.exception.JDBClusterException;
import org.jdbcluster.tracing.TracingAspect;
import org.apache.log4j.Logger;

/**
 * abstract aspect for JDBCluster logging
 */
public abstract aspect JDBClusterLogging extends LoggingAspect {
	
	public pointcut logExceptionThrow( ):
		call(* *(..) throws JDBClusterException+);
	
	public pointcut exceptionsToBeLogged( ) :
		handler(org.jdbcluster.exception.JDBClusterException);

	protected abstract Logger getLogger();
	
	protected void traceBefore(JoinPoint joinPoint, Object caller) {
		getLogger().info("Called " + joinPoint.getSignature( ));
	}
	
	protected void traceStaticBefore(JoinPoint joinPoint) {
		getLogger().info("Statically Called " + joinPoint.getSignature());
	}
	
	protected void traceAfter(JoinPoint joinPoint, Object object) {
		getLogger().info("Returned from " + joinPoint.getSignature());
	}
	
	protected void traceStaticAfter(JoinPoint joinPoint) {
		getLogger().info("Returned from static call to " + joinPoint.getSignature());
	}
	
	protected void logException(JoinPoint joinPoint) {
		getLogger().info("" + joinPoint.getArgs()[0] + " exception thrown");
	}
	
	protected void logExceptionThrow(JoinPoint joinPoint) {
		getLogger().error("" + joinPoint.getSignature() + " exception thrown");
	}
	
	private static aspect FormatCallDepthAspect {
		private static int callDepth;
		
		private pointcut captureTraceBefore( ) :
			call(protected void TracingAspect.trace*Before(..));
		
		private pointcut captureTraceAfter( ) :
			call(protected void TracingAspect.trace*After(..));
		
		after( ) : captureTraceBefore( )
		{
			callDepth++;
		}
		
		before( ) : captureTraceAfter( )
		{
			callDepth--;
		}
		
		private pointcut captureMessageOutput(String message) :
			(call(* *.info(Object)) ||
			call(* *.error(Object))) &&
			args(message) &&
			within(JDBClusterLogging) &&
			!within(FormatCallDepthAspect);
		
		Object around(String originalMessage) : captureMessageOutput(originalMessage)
		{
			StringBuffer buffer = new StringBuffer( );
			for (int x = 0; x < callDepth; x++)
			{
				buffer.append("--");
			}
			buffer.append(originalMessage);
			return proceed(buffer.toString( ));
		}
	}
}
