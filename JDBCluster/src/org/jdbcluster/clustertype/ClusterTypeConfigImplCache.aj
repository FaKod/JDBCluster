package org.jdbcluster.clustertype;

import org.jdbcluster.metapersistence.aspects.XmlCache;
import org.jdbcluster.metapersistence.aspects.XmlCachable;

/**
 * Implementing cache of class ClusterTypeConfigImpl
 * @author FaKod
 */
public aspect ClusterTypeConfigImplCache extends XmlCache {
	
	/**
	 * mark class as cachable
	 */
	declare parents : ClusterTypeConfigImpl implements XmlCachable;

	/**
	 * pointcut for no parameter methods
	 */
	public pointcut xmlCache0(XmlCachable xmlc):
		execution(public * XmlCachable+.get*()) &&
		!execution(public * XmlCachable+.getConfiguration()) && 
		within(ClusterTypeConfigImpl) &&
		target(xmlc);
	
	/**
	 * pointcut for one String parameter methods
	 */
	public pointcut xmlCache1(XmlCachable xmlc, String str):
		execution(public * XmlCachable+.get*(String)) && 
		within(ClusterTypeConfigImpl) &&
		args(str) &&
		target(xmlc);
	
	/**
	 * pointcut for two String parameter methods
	 */
	public pointcut xmlCache2(XmlCachable xmlc, String str, String str2):
		execution(public * XmlCachable+.get*(String, String)) && 
		within(ClusterTypeConfigImpl) &&
		args(str, str2) &&
		target(xmlc);
}
