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
public aspect CQueryAspect extends CAspect {
	
	pointcut resultList(HibernateQuery q):
		execution(* HibernateQuery+.list(..)) && 
		within(HibernateQuery) && 
		target(q);

	Object around(HibernateQuery q):resultList(q) {
		ClusterType ct = q.getClusterType();
		List dAOResultSet = (List) proceed(q);
		List<Cluster> clusterResultSet = new ArrayList<Cluster>();
		
		for (Object dao : dAOResultSet) {
			Cluster c = ClusterFactory.newInstance(ct);
			c.dao = (Dao) dao;
			clusterResultSet.add(c);
		}
		return clusterResultSet;
	}
}
