package test.domain;

import junit.framework.Assert;
import junit.framework.TestCase;
import mycluster.CBicycle;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;
import org.jdbcluster.template.SessionTemplate;

public class TestDomainCheck2 extends TestCase {

	SessionTemplate session;

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");

		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.TestDomainCheck2.xml");
		super.setUp();
	}

	public void testDomainValids() {

		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType, null);
		
		/*
		 * set ColorType to null should be possible
		 */
		rad.setColorType(null);
		
		/*
		 *  set Color to null should fail (no wildcard "*" entry)
		 */
		try {
			rad.setColor(null);
			Assert.fail("null is not allowed with ColorType="+rad.getColorType());
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
		
		/*
		 * set ColorShading to null should be OK (wildcard "*" entry)
		 */
		rad.setColorShading(null);
		
		/*
		 * set ColorShading to LightRED should be OK (wildcard "*" entry)
		 */
		rad.setColorShading("LightRED");
		
		/*
		 * Set Color to "RED" with ColorType == null should fail
		 */
		try {
			rad.setColor("RED");
			Assert.fail("RED is not allowed with ColorType="+rad.getColorType());
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
		

		/**
		 * Tests if it's possible to add an attribute (GREY) not listed in vde
		 * via XML valid tag only. This should NOT be allowed.
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
	}
	
	public void testDomainInvalids() {

		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType, null);
		
		/**
		 * Test for invalid tag. Everything in the vde list should be allowed,
		 * except BLACK which is in an invalid tag.
		 */
		rad.setColorType("BlackWhite");
		// following values are OK for "Color"
		rad.setColor("WHITE");
		rad.setColor("GREY50%");
		rad.setColor("GREEN");
		try {
			rad.setColor("BLACK");
			Assert.fail("BLACK is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
		
		try {
			rad.setColor("IMPOSSIBLE COLOR");
			Assert.fail("IMPOSSIBLE COLOR is not allowed");
		} catch (Exception e) {
			assertTrue(e instanceof DomainException);
		}
	}
}
