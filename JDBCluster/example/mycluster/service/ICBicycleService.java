package mycluster.service;

import mycluster.CBicycle;

import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.annotation.PrivilegesService;
import org.jdbcluster.service.PrivilegedService;

@PrivilegesService(required="BIKESERVCIE")
public interface ICBicycleService extends PrivilegedService {

	@PrivilegesMethod(required="BIKEPAINT")
	void makeBicycleRED(CBicycle bike);
}
