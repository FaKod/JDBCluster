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
package org.jdbcluster.tracing;

import org.aspectj.lang.JoinPoint;
import org.apache.log4j.Logger;

/**
 * @author Christopher Schmidt
 */
public abstract aspect TracingAspect {
	
	protected abstract Logger getLogger();
	
	public abstract pointcut pointsToBeTraced();

	public abstract pointcut pointsToBeExcluded();

	public pointcut filteredPointsToBeTraced(Object caller) :
		pointsToBeTraced( ) &&
		!pointsToBeExcluded( ) &&
		!within(org.jdbcluster.tracing.TracingAspect+) &&
		this(caller);

	public pointcut catchStaticCallers() :
		pointsToBeTraced( ) &&
		!pointsToBeExcluded( ) &&
		!within(org.jdbcluster.tracing.TracingAspect+) &&
		!filteredPointsToBeTraced(Object);

	before(Object caller) : filteredPointsToBeTraced(caller) {
		traceBefore(thisJoinPoint, caller);
	}

	before() : catchStaticCallers( ) {
		traceStaticBefore(thisJoinPoint);
	}

	after(Object caller) : filteredPointsToBeTraced(caller) {
		traceAfter(thisJoinPoint, caller);
	}

	after() : catchStaticCallers( ) {
		traceStaticAfter(thisJoinPoint);
	}

	protected void traceBefore(JoinPoint joinPoint, Object caller) {
		getLogger().info(caller + " calling " + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceStaticBefore(JoinPoint joinPoint) {
		getLogger().info("Static code calling " + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceAfter(JoinPoint joinPoint, Object caller) {
		getLogger().info("Returning from call to" + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceStaticAfter(JoinPoint joinPoint) {
		getLogger().info("Returning from static call to "
				+ joinPoint.getSignature() + " @ "
				+ joinPoint.getSourceLocation());
	}

	private static aspect FormatCallDepthAspect {
		private static int callDepth;

		private pointcut captureTraceBefore() : 
			call(protected void TracingAspect.trace*Before(..));

		private pointcut captureTraceAfter() :	
			call(protected void TracingAspect.trace*After(..));

		after() : captureTraceBefore( ) {
			callDepth++;
		}

		before() : captureTraceAfter( ) {
			callDepth--;
		}

		private pointcut captureMessageOutput(String message) :
			call(* *.info(Object)) &&
			args(message) &&
			within(TracingAspect) &&
			!within(FormatCallDepthAspect);

		Object around(String originalMessage) : captureMessageOutput(originalMessage)
		{
			StringBuffer buffer = new StringBuffer();
			for (int x = 0; x < callDepth; x++) {
				buffer.append("--");
			}
			buffer.append(originalMessage);
			return proceed(buffer.toString());
		}
	}
}
