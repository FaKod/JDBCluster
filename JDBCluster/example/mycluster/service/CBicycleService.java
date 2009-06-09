package mycluster.service;

import mycluster.CBicycle;

import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.annotation.PrivilegesParameter;
import org.jdbcluster.metapersistence.annotation.PrivilegesService;
import org.jdbcluster.metapersistence.security.user.IUser;
import org.jdbcluster.service.PrivilegedService;

@PrivilegesService(required="BIKESERVCIE")
public class CBicycleService implements ICBicycleService, PrivilegedService {

	@PrivilegesMethod(required="BIKEPAINT")
	public void makeBicycleRED(@PrivilegesParameter(property={"color"}) CBicycle bike) {
	}
	
	@PrivilegesMethod(required="BIKEPAINT_CP")
	public void makeBicycleCheckParam(@PrivilegesParameter(property={"colorType"}) CBicycle bike) {
	}
	
	@PrivilegesMethod(required="BIKEPAINT_CP2")
	public void makeBicycleCheckParam2(@PrivilegesParameter(property={"colorShading"}) CBicycle bike) {
	}

	public IUser getUser() {
		return null;
	}

}
