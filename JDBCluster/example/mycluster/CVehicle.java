package mycluster;

import org.jdbcluster.dao.Dao;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.privilege.PrivilegedCluster;

@DaoLink(dAOClass = Dao.class)
public abstract class CVehicle extends Cluster implements PrivilegedCluster, IVehicle {

	long id;
	
	@NoDAO
	NestedPropertyForPrivTest nested;

	@PrivilegesMethod(required={"GETNESTED"})
	public NestedPropertyForPrivTest getNested() {
		return nested;
	}

	public void setNested(NestedPropertyForPrivTest nested) {
		this.nested = nested;
	}
	
	public long getId() {
		return id;
	}
}
