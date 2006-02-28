package org.jdbcluster.metapersistence.aspects;

import org.jdbcluster.metapersistence.cluster.Cluster;

/**
 * 
 * @author Chistopher Schmidt
 *
 */
public interface ICluster {
	Cluster loadById(long id);
}
