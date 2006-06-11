package org.jdbcluster.metapersistence.aspects;

import java.util.HashMap;
import java.lang.reflect.Method;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.metapersistence.aspects.XmlCache;

public abstract aspect XmlCache {
	
	private HashMap<Method, HashMap<ClusterType, HashMap<String, Object>>> 
	XmlCachable.cache = new HashMap<Method, HashMap<ClusterType, HashMap<String, Object>>>(); 

	
	protected void fillCache(XmlCachable xmlc, Method m, ClusterType ct, Object value, String... key) {
		HashMap<ClusterType, HashMap<String, Object>> hm = xmlc.cache.get(m);
		if(hm==null) {
			hm = new HashMap<ClusterType, HashMap<String, Object>>();
			xmlc.cache.put(m, hm);
		}
		HashMap<String, Object> hm2 = hm.get(ct);
		if(hm2==null) {
			hm2 = new HashMap<String, Object>();
			hm.put(ct, hm2);
		}
		hm2.put(doKey(key), value);
	}
	
	protected Object getCache(XmlCachable xmlc, Method m, ClusterType ct, String... key) {
		HashMap<ClusterType, HashMap<String, Object>> hm = xmlc.cache.get(m);
		HashMap<String, Object> hm2 = hm.get(ct);
		return hm2.get(doKey(key)); 
	}
	
	protected boolean isInCache(XmlCachable xmlc, Method m, ClusterType ct, String... key) {
		HashMap<ClusterType, HashMap<String, Object>> hm = xmlc.cache.get(m);
		if(hm==null)
			return false;
		HashMap<String, Object> hm2 = hm.get(ct);
		if(hm2==null) 
			return false;
		return hm2.containsKey(doKey(key));
	}
	
	protected String doKey(String... key) {
		StringBuffer res = new StringBuffer();
		for(String str: key) {
			res.append(str);
		}
		return res.toString();
	}
}
