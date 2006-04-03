package test.privilege;

import mycluster.CBicycle;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

import junit.framework.TestCase;

public class TestPrivilege extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.TestDomainCheck.xml");
		//JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");
		super.setUp();
	}

	public void testPrivilege() {
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);

		/**
		 * 
		 */
		rad.setColorType("Color");
		
		rad.setColor("RED");
		
		rad.setColorShading("LightRED");
		
	}
}
