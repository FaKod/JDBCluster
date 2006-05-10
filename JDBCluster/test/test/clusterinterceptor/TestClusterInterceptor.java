package test.clusterinterceptor;

import mycluster.CBicycle;
import mycluster.CCar;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

import junit.framework.Assert;
import junit.framework.TestCase;

public class TestClusterInterceptor extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig
				.setConfiguration("test/test/clusterinterceptor/jdbcluster.conf.ClusterInterceptor.xml");
		super.setUp();
	}

	public void testDomainOneDep() {
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);

		// we use cluster auto now
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");

		// create a Cluster and persist it
		// but interceptor returning false -> exception
		try {
			CCar bmw = ClusterFactory.newInstance(cAutoType);
			Assert.fail("bmw Interceptor should return false");
		} catch (ConfigurationException e) {
			System.out.println(e.getMessage());
		}
	}
}
