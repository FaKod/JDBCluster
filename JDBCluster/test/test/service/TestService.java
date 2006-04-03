package test.service;

import mycluster.CBicycle;
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

		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.xml");
		super.setUp();
	}

	public void testService() {
//		 we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		ICBicycleService bs = new CBicycleService();
		bs.makeBicycleRED(rad);
	}
}
