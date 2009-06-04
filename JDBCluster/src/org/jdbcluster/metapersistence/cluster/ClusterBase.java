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

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.metapersistence.security.user.IUser;
import org.springframework.util.Assert;

public abstract class ClusterBase implements ICluster {

	private Object dao;
	
	private IUser user;
	
	private Class<? extends Object> daoClass;

	private ClusterType clusterType;

	public Object getDao() {
		return dao;
	}

	public void setDao(Object dao) {

		Assert.notNull(dao, "dao may not be null");

		/*
		 * @TODO
		 * This was a try to implement a assignment check. Stay tuned ;-)
		 */
//		if (false) {
//			if (daoClass != null) {
//				if (!daoClass.equals(dao.getClass()) && !daoClass.isAssignableFrom(dao.getClass())) {
//					throw new DaoException("Assigning wrong Dao type in ClusterType [" + ((clusterType != null) ? clusterType.getClusterClassName() : "unknown") + "]. Dao class is [" + dao.getClass().getName()
//							+ "] and should be [" + daoClass.getName() + "]");
//				}
//			} else
//				throw new DaoException("No Dao class set for ClusterType [" + ((clusterType != null) ? clusterType.getClusterClassName() : "unknown") + "]");
//
//		}

		this.dao = dao;
		this.daoClass = dao.getClass();
	}
		
	public void setUser(IUser user) {
		this.user = user;
	}
	
	public IUser getUser() {
		return user;
	}

	public void setClusterType(ClusterType ct) {

		Assert.notNull(ct, "ClusterType may not be null");

		clusterType = ct;
	}

	public ClusterType getClusterType() {
		return clusterType;
	}

	public Class<? extends Object> getDaoClass() {
		return daoClass;
	}

	public void setDaoClass(Class<? extends Object> daoClass) {

		Assert.notNull(daoClass, "daoClass may not be null");

		this.daoClass = daoClass;
	}
	
	public Class<? extends Cluster> getClusterClass() {
		return clusterType.getClusterClass();
	}
		
}
