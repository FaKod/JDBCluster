package org.jdbcluster.tracing;

import org.aspectj.lang.JoinPoint;

public abstract aspect TracingAspect {
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
		System.out.println(caller + " calling " + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceStaticBefore(JoinPoint joinPoint) {
		System.out.println("Static code calling " + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceAfter(JoinPoint joinPoint, Object caller) {
		System.out.println("Returning from call to" + joinPoint.getSignature()
				+ " @ " + joinPoint.getSourceLocation());
	}

	protected void traceStaticAfter(JoinPoint joinPoint) {
		System.out.println("Returning from static call to "
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
			call(* *.println(String)) &&
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
