package test.service;

import java.util.Set;

import mycluster.CBicycle;
import mycluster.privilege.UserPrivilege;
import mycluster.service.CBicycleService;
import mycluster.service.ICBicycleService;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

import junit.framework.TestCase;

public class TestService extends TestCase {
	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.TestDomainCheck.xml");
		super.setUp();
	}

	public void testService() {
		Set<String> lastPrivSet;
		ICBicycleService bs = new CBicycleService();
		
//		 we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		/*
		 * check an empty CBicycle object
		 */
		UserPrivilege.clearLastprivSet();
		
		bs.makeBicycleRED(rad);
		
		lastPrivSet = UserPrivilege.getPrivSet();
		assertTrue(lastPrivSet.size()==5);
		assertTrue(lastPrivSet.contains("BIKE"));
		assertTrue(lastPrivSet.contains("GETCOLOR"));
		assertTrue(lastPrivSet.contains("NeedsRight4[Color]DomainValue[NULL]"));
		assertTrue(lastPrivSet.contains("BIKESERVCIE"));
		assertTrue(lastPrivSet.contains("BIKEPAINT"));
		
		/**
		 * fill with some values
		 */
		rad.setColorType("Color");
		rad.setColor("RED");
		
		UserPrivilege.clearLastprivSet();
		
		bs.makeBicycleRED(rad);
		
		lastPrivSet = UserPrivilege.getPrivSet();
		assertTrue(lastPrivSet.size()==5);
		assertTrue(lastPrivSet.contains("BIKE"));
		assertTrue(lastPrivSet.contains("GETCOLOR"));
		assertTrue(lastPrivSet.contains("NeedsRight4[Color]DomainValue[RED]"));
		assertTrue(lastPrivSet.contains("BIKESERVCIE"));
		assertTrue(lastPrivSet.contains("BIKEPAINT"));
	}
}
