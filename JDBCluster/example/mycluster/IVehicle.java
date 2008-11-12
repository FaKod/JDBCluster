package mycluster;

import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.cluster.ICluster;

public interface IVehicle extends ICluster{

	@PrivilegesMethod(required = { "GETNESTED" })
	NestedPropertyForPrivTest getNested();

	void setNested(NestedPropertyForPrivTest nested);

	long getId();

}