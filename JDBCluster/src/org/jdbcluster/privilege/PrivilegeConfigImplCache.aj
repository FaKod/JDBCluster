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
package org.jdbcluster.privilege;

import org.jdbcluster.metapersistence.aspects.XmlCache;
import org.jdbcluster.metapersistence.aspects.XmlCachable;

/**
 * Implementing cache of class PrivilegeConfigImpl
 * @author FaKod
 */
public aspect PrivilegeConfigImplCache extends XmlCache {
	
	/**
	 * mark class as cachable
	 */
	declare parents : PrivilegeConfigImpl implements XmlCachable;

	/**
	 * pointcut for no parameter methods
	 */
	public pointcut xmlCache0(XmlCachable xmlc):
		execution(public * XmlCachable+.get*()) &&
		!execution(public * XmlCachable+.getConfiguration()) && 
		within(PrivilegeConfigImpl) &&
		target(xmlc);
	
	/**
	 * pointcut for one String parameter methods
	 */
	public pointcut xmlCache1(XmlCachable xmlc, String str):
		execution(public * XmlCachable+.get*(String)) && 
		within(PrivilegeConfigImpl) &&
		args(str) &&
		target(xmlc);
	
	/**
	 * pointcut for two String parameter methods
	 */
	public pointcut xmlCache2(XmlCachable xmlc, String str, String str2):
		execution(public * XmlCachable+.get*(String, String)) && 
		within(PrivilegeConfigImpl) &&
		args(str, str2) &&
		target(xmlc);

}
