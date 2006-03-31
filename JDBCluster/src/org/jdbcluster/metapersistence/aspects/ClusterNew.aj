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

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * @author Christopher Schmidt
 * instanciate a new cluster means create a new Dao object
 */
public aspect ClusterNew extends ClusterBaseAspect {

	/**
	 * join points for creation of Cluster Objects
	 */
	pointcut newCluster(Cluster c) : 
		initialization(Cluster.new(..)) && 
		target(c);

	after(Cluster c) returning : newCluster(c) {
		//get the Class Annotations
		DaoLink classAnno = c.getClass().getAnnotation(DaoLink.class);
		if (classAnno != null) {
			c.dao = (Dao) JDBClusterUtil.createClassObject(classAnno.dAOClass());
			c.daoClass = classAnno.dAOClass();
		}
	}
}
