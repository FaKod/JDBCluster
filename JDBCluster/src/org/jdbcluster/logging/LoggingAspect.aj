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

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.SuppressAjWarnings;
import org.jdbcluster.tracing.TracingAspect;

/**
 * @author Christopher Schmidt
 * abstract aspect 
 */
public abstract aspect LoggingAspect extends TracingAspect {
	
	protected abstract pointcut logExceptionThrow( );
	
	protected abstract pointcut exceptionsToBeLogged( );
	
	private pointcut filteredExceptionCapture( ) :
				exceptionsToBeLogged( ) &&
				!pointsToBeExcluded( );
	
	@SuppressAjWarnings("adviceDidNotMatch")
	before( ) : filteredExceptionCapture( )
	{
		logException(thisJoinPoint);
	}
	
	@SuppressAjWarnings("adviceDidNotMatch")
	after( ) throwing: logExceptionThrow( )
	{
		logExceptionThrow(thisJoinPoint);
	}
	
	protected abstract void logException(JoinPoint joinPoint);
	protected abstract void logExceptionThrow(JoinPoint joinPoint);
}
