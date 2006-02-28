package org.jdbcluster.metapersistence.aspects;

import org.jdbcluster.JDBClusterUtil;
import org.jdbcluster.dao.Dao;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * @author Christopher Schmidt
 * instanciate a new cluster means create a new Dao object
 */
public aspect ClusterNew extends CAspect {

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
