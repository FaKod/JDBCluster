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

import org.jdbcluster.template.hibernate.HibernateQuery;
import org.jdbcluster.clustertype.ClusterType;
import java.util.List;
import java.util.ArrayList;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.dao.Dao;

/**
 * @author Christopher Schmidt
 * Aspect used after a native (fe. hibernate) query was
 * executed. We have to convert Dao objects
 * to Cluster objects
 */
public aspect ClusterQuery extends ClusterBaseAspect {
	
	pointcut resultList(HibernateQuery q):
		execution(* HibernateQuery+.list(..)) && 
		within(HibernateQuery) && 
		target(q);

	Object around(HibernateQuery q):resultList(q) {
		ClusterType ct = q.getClusterType();
		List dAOResultSet = (List) proceed(q);
		List<Cluster> clusterResultSet = new ArrayList<Cluster>();
		
		for (Object dao : dAOResultSet) {
			Cluster c = ClusterFactory.newInstance(ct, (Dao) dao);
			clusterResultSet.add(c);
		}
		return clusterResultSet;
	}
}
