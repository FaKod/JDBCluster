package mycluster;

import org.jdbcluster.metapersistence.cluster.Cluster;

public abstract class CVehicle extends Cluster {

	long id;
	
	public long getId() {
		return id;
	}
}
