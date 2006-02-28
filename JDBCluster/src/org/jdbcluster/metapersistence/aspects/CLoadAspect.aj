package org.jdbcluster.metapersistence.aspects;

import org.jdbcluster.metapersistence.cluster.*;

/**
 * @author Christopher Schmidt
 * there must be a reason why ive written this
 */
public aspect CLoadAspect extends CAspect {

	declare parents : Cluster implements ICluster;

	public Cluster Cluster.loadById(long id) {
		return null;
	}
}
