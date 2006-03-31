package test.domain;

import junit.framework.Assert;
import junit.framework.TestCase;
import mycluster.CBicycle;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.domain.DomainChecker;
import org.jdbcluster.domain.DomainCheckerImpl;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.template.SessionTemplate;

public class TestDomainCheck extends TestCase {

	SessionTemplate session;

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.xml");
		//JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");
		super.setUp();
	}

	public void testDomainOneDep() {

		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);

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
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
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
	
	public void testDomainCheckWithPath() {
		DomainChecker dc = new DomainCheckerImpl();
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		rad.setColorType("Color");
		// following values are OK for "Color"
		rad.setColor("RED");
		
		boolean shouldBeTrue = dc.check(rad, "color");
		assertTrue(shouldBeTrue);
		
		rad.setColorType("BlackWhite");
		boolean shouldBeFalse = dc.check(rad, "color");
		assertFalse(shouldBeFalse);
	}
	
	public void testAdditionalMaster() {
		DomainChecker dc = new DomainCheckerImpl();
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		rad.setColorType("All");
		// following values are OK for "Color"
		rad.setColor("RED");
		
		rad.setColorShading("MiddleRED");
		boolean shouldBeTrue = dc.check(rad, "colorShading");
		assertTrue(shouldBeTrue);
		
		rad.setColorShading("DarkRED");
		shouldBeTrue = dc.check(rad, "colorShading");
		assertTrue(shouldBeTrue);
		
		try {
			rad.setColorShading("LightRED");
			Assert.fail("LightRED is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
	}
}
