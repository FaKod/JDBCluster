package test;

import junit.framework.Assert;
import junit.framework.TestCase;
import mycluster.CFahrrad;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.template.SessionTemplate;

public class TestDomainCheck extends TestCase {

	SessionTemplate session;

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("xml/jdbcluster.conf.xml");
		JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");
		super.setUp();
	}

	public void testDomainOneDep() {

		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("fahrrad");

		// create a Cluster and persist it
		CFahrrad rad = ClusterFactory.newInstance(cFahrradType);

		/**
		 * 
		 */
		rad.setColorType("Color");
		// following values are OK for "Color"
		rad.setColor("RED");
		rad.setColor("GREEN");
		rad.setColor("BLUE");
		try {
			rad.setColor("GREY");
			Assert.fail("GREY is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}

		/**
		 * 
		 */
		rad.setColorType("BlackWhite");
		// following values are OK for "Color"
		rad.setColor("BLACK");
		rad.setColor("WHITE");
		rad.setColor("GREY50%");
		try {
			rad.setColor("RED");
			Assert.fail("RED is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
		try {
			rad.setColor("GREEN");
			Assert.fail("GREEN is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}

		/**
		 * 
		 */
		rad.setColorType("None");
		rad.setColor(null);
		try {
			rad.setColor("GREEN");
			Assert.fail("GREEN is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}

		rad.setColorType("All");
		rad.setColor(null);
		rad.setColor("RED");
		rad.setColor("GREEN");
		rad.setColor("BLUE");
		rad.setColor("BLACK");
		rad.setColor("WHITE");
		rad.setColor("GREY50%");

	}
	
	public void testDomainTwoDep() {

		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("fahrrad");

		// create a Cluster and persist it
		CFahrrad rad = ClusterFactory.newInstance(cFahrradType);
		
		rad.setColorType("Color");
		// following values are OK for "Color"
		rad.setColor("RED");
		
		rad.setColorShading("LightRED");
		rad.setColorShading("DarkRED");
		try {
			rad.setColorShading("LightGREEN");
			Assert.fail("LightGREEN is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
		
		// wildcard matching (only "RED" is defined
		rad.setColor("GREEN");
		rad.setColorShading("LightRED");
		rad.setColorShading("DarkRED");
	}
}
