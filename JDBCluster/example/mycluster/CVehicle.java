package mycluster;

import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.Cluster;

import dao.Vehicle;

@DaoLink(dAOClass = Vehicle.class)
public abstract class CVehicle extends Cluster {

	long id;
	
	public long getId() {
		return id;
	}
}
