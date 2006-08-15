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
package org.jdbcluster.metapersistence.cluster;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeBase;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.exception.ClusterTypeException;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.exception.PrivilegeException;
import org.jdbcluster.privilege.PrivilegeChecker;
import org.jdbcluster.privilege.PrivilegeCheckerImpl;
import org.jdbcluster.privilege.PrivilegedCluster;
import org.springframework.util.Assert;

/**
 * class ClusterFactory is responsible for creating instances
 * of type Cluster. <b>Static</b> privileges are checked <b>after</b>
 * calling ClusterInterceptor
 * @author Philipp Noggler
 * @author FaKod
 *
 */
public abstract class ClusterFactory {
	
	private static ClusterInterceptor clusterInterceptor;

	/**
	 * creates an instance of a Cluster
	 * @param ct specifies the ClusterType class that should be returned
	 * @return Cluster
	 */
	public static <T extends Cluster> T newInstance(ClusterType ct) {
		return newInstance(ct, null);
	}
	
	/**
	 * no need to create ClusterType instance
	 * @param clusterType cluster type string as configured
	 * @return cluster instance
	 */
	public static <T extends Cluster> T newInstance(String clusterType) {
		ClusterType ct = ClusterTypeFactory.newInstance(clusterType);
		return newInstance(ct, null);
	}
	
	/**
	 * no need to create ClusterType instance
	 * @param clusterType cluster type string as configured
	 * @return cluster instance
	 */
	public static <T extends Cluster> T newInstance(String clusterType, Dao dao) {
		ClusterType ct = ClusterTypeFactory.newInstance(clusterType);
		return newInstance(ct, dao);
	}
	
	/**
	 * creates and return the configured ClusterInterceptor
	 * @return ClusterInterceptor
	 */
	public static ClusterInterceptor getClusterInterceptor() {
		if(clusterInterceptor==null) {
			String ciStr = ClusterTypeBase.getClusterTypeConfig().getClusterInterceptorClassName();
			if(ciStr!=null && ciStr.length()>0 )
				clusterInterceptor = (ClusterInterceptor) JDBClusterUtil.createClassObject(ciStr);
			else
				clusterInterceptor = (ClusterInterceptor) JDBClusterUtil.createClassObject(DefaultClusterInterceptor.class.getName());
		}
		return clusterInterceptor;
	}
	
	/**
	 * creates an instance of a Cluster
	 * Cluster interceptor is <b>not called</b> if dao!=null
	 * Cluster privileges are <b>not checked</b> if dao!=null 
	 * @param ct specifies the ClusterType class that should be returned
	 * @param dao dao object to be presetted
	 * @return Cluster
	 */
	public static <T extends Cluster> T newInstance(ClusterType ct, Dao dao) {
		T cluster = newInstance(getClusterClass(ct), dao);
		cluster.setClusterType(ct);
		return cluster;
	}
	
	/**
	 * creates an instance of a Cluster
	 * Cluster interceptor is <b>not called</b> if dao!=null
	 * Cluster privileges are <b>not checked</b> if dao!=null 
	 * If dao is not null its assumed that the dao object is persistent 
	 * @param clusterClass class of cluster
	 * @param dao dao object to be presetted
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cluster> T newInstance(Class<?> clusterClass, Dao dao) {
		return newInstance(clusterClass, dao, dao!=null);
	}
	
	/**
	 * creates an instance of a Cluster
	 * Cluster interceptor is <b>not called</b> if dao!=null
	 * Cluster privileges are <b>not checked</b> if dao!=null 
	 * @param clusterClass class of cluster
	 * @param dao dao object to be presetted
	 * @param daoIsPersistent if dao Object is persistent dont call interceptor and pivilegeInterceptor
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Cluster> T newInstance(Class<?> clusterClass, Dao dao, boolean daoIsPersistent) {
		
		Assert.notNull(clusterClass, "Class<?> may not be null");
		
		PrivilegeChecker pc = PrivilegeCheckerImpl.getInstance();
		Cluster cluster = null;
		try {
			//create a new instance with given classname
			cluster = (Cluster) clusterClass.newInstance();
			if(dao!=null)
				cluster.setDao(dao);
		} catch (InstantiationException e) {
			throw new ClusterTypeException("specified class [" + clusterClass.getName() + "] object cannot be instantiated because it is an interface or is an abstract class", e);
		} catch (IllegalAccessException e) {
			throw new ClusterTypeException("the currently executed ctor for class [" + clusterClass.getName() + "] does not have access", e);
		} 
		
		if(!daoIsPersistent) {
			/*
			 * call of cluster interceptor
			 */
			if(!getClusterInterceptor().clusterNew(cluster))
				throw new ConfigurationException("ClusterInterceptor [" + getClusterInterceptor().getClass().getName() + "] returned false" );
			
			/*
			 * privilege check (only static privileges are checked)
			 */
			if(cluster instanceof PrivilegedCluster) {
				if(!pc.userPrivilegeIntersect((PrivilegedCluster)cluster))
					throw new PrivilegeException("No sufficient privileges for new Cluster class [" + clusterClass.getName() + "]");
			}
		}
		return (T) cluster;
	}
	
	/**
	 * get the cluster class object
	 * @param ct specifies the ClusterType class that should be returned
	 * @return Class<? extends ClusterBase> of ClusterType
	 */
	@SuppressWarnings("unchecked")
	public static Class<? extends ClusterBase> getClusterClass(ClusterType ct) {
		
		Assert.notNull(ct, "ClusterType may not be null");
		
		String className = ClusterTypeBase.getClusterTypeConfig().getClusterClassName(ct.getName());
		if (className == null) {
			throw new ConfigurationException("unknown ClusterType [" + ct.getName() + "]");
		}

		Class<? extends ClusterBase> clusterClass;
		try {
			clusterClass = (Class<? extends ClusterBase>) Class.forName(className, false, Thread.currentThread().getContextClassLoader());
		} catch (ClassNotFoundException e) {
			throw new ClusterTypeException("no definition for the class [" + className + "] with the specified name could be found", e);
		}
		
		return clusterClass;
	}
	
	/**
	 *  get the cluster class object
	 * @param clusterType specifies the ClusterType class that should be returned
	 * @return Class<? extends ClusterBase> of ClusterType
	 */
	public static Class<? extends ClusterBase> getClusterClass(String clusterType) {
		
		Assert.notNull(clusterType, "String clusterType may not be null");
		
		ClusterType ct = ClusterTypeFactory.newInstance(clusterType);
		return getClusterClass(ct);
	}
	
}
