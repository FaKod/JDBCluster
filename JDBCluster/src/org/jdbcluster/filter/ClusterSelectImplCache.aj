package org.jdbcluster.filter;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.metapersistence.aspects.XmlCache;
import org.jdbcluster.metapersistence.aspects.XmlCachable;
import org.aspectj.lang.reflect.MethodSignature;

public aspect ClusterSelectImplCache extends XmlCache {
	
	declare parents : ClusterSelectImpl implements XmlCachable;

	pointcut doCache(XmlCachable xmlc, ClusterType ct, String str):
		execution(public * XmlCachable+.*(ClusterType, String)) && 
		args(ct, str) &&
		target(xmlc);
	
	pointcut doCache2(XmlCachable xmlc, ClusterType ct, String str, String str2):
		execution(public * XmlCachable+.*(ClusterType, String, String)) && 
		args(ct, str, str2) &&
		target(xmlc);

	Object around(XmlCachable xmlc, ClusterType ct, String str):doCache(xmlc, ct, str) {
		MethodSignature ms = (MethodSignature) thisJoinPoint.getSignature();
		if(isInCache(xmlc, ms.getMethod(), ct, str))
			return getCache(xmlc, ms.getMethod(), ct, str);
		Object o = proceed(xmlc, ct, str);
		fillCache(xmlc, ms.getMethod(), ct, o, str);
		return o;
	}
	
	Object around(XmlCachable xmlc, ClusterType ct, String str, String str2):doCache2(xmlc, ct, str, str2) {
		MethodSignature ms = (MethodSignature) thisJoinPoint.getSignature();
		if(isInCache(xmlc, ms.getMethod(), ct, str, str2))
			return getCache(xmlc, ms.getMethod(), ct, str, str2);
		Object o = proceed(xmlc, ct, str, str2);
		fillCache(xmlc, ms.getMethod(), ct, o, str, str2);
		return o;
	}
	
	
}
