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
package org.jdbcluster.metapersistence.aspects;

import java.util.HashMap;
import java.lang.reflect.Method;
import org.jdbcluster.metapersistence.aspects.XmlCache;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * Aspect to cache the various XML XPATH operations
 * the class used for this must implement interface XmlCachable
 * the Caching is implemented flexible to allow implementing additional methods
 * in the XPATH classes (Its not the fastest caching way)
 * @author FaKod
 */
public abstract aspect XmlCache {
	
	/**
	 * intertype declation on all XmlCachable interfaces
	 */
	private HashMap<Method, HashMap<String, Object>> XmlCachable.cache = new HashMap<Method, HashMap<String, Object>>(); 

	public abstract pointcut xmlCache0(XmlCachable xmlc);
	public abstract pointcut xmlCache1(XmlCachable xmlc, String str);
	public abstract pointcut xmlCache2(XmlCachable xmlc, String str, String str2);
	
	/**
	 * around advice for no parameter methods
	 */
	Object around(XmlCachable xmlc) : xmlCache0(xmlc) {
		MethodSignature ms = (MethodSignature) thisJoinPoint.getSignature();
		if(isInCache(xmlc, ms.getMethod()))
			return getCache(xmlc, ms.getMethod());
		
		Object o = proceed(xmlc);
		fillCache(xmlc, ms.getMethod(), o);
		return o;
	}

	/**
	 * around advice for one String parameter methods
	 */
	Object around(XmlCachable xmlc, String str) : xmlCache1(xmlc, str) {
		MethodSignature ms = (MethodSignature) thisJoinPoint.getSignature();
		if(isInCache(xmlc, ms.getMethod(), str))
			return getCache(xmlc, ms.getMethod(), str);
		
		Object o = proceed(xmlc, str);
		fillCache(xmlc, ms.getMethod(), o, str);
		return o;
	}
	
	/**
	 * around advice for two String parameter methods
	 */
	Object around(XmlCachable xmlc, String str, String str2) : xmlCache2(xmlc, str, str2) {
		MethodSignature ms = (MethodSignature) thisJoinPoint.getSignature();
		if(isInCache(xmlc, ms.getMethod(), str, str2))
			return getCache(xmlc, ms.getMethod(), str, str2);
		
		Object o = proceed(xmlc, str, str2);
		fillCache(xmlc, ms.getMethod(), o, str, str2);
		return o;
	}
	
	/**
	 * filles cache with a concat. String as Key and a object as value
	 */
	protected void fillCache(XmlCachable xmlc, Method m, Object value, String... key) {
		HashMap<String, Object> hm = xmlc.cache.get(m);
		if(hm==null) {
			hm = new HashMap<String, Object>();
			xmlc.cache.put(m, hm);
		}
		hm.put(doKey(key), value);
	}
	
	/**
	 * gets a Object from the cache
	 * null is a valid value! Please check isInCache first
	 */
	protected Object getCache(XmlCachable xmlc, Method m, String... key) {
		HashMap<String, Object> hm = xmlc.cache.get(m);
		return hm.get(doKey(key)); 
	}
	
	/**
	 * checks if a value is inserted in the cache
	 */
	protected boolean isInCache(XmlCachable xmlc, Method m, String... key) {
		HashMap<String, Object> hm = xmlc.cache.get(m);
		if(hm==null) 
			return false;
		return hm.containsKey(doKey(key));
	}
	
	/**
	 * cancats the key values
	 */
	private String doKey(String... key) {
		if(key==null || key.length==0) 
			return null;
		StringBuffer res = new StringBuffer();
		for(String str: key) {
			res.append(str);
		}
		return res.toString();
	}
}
