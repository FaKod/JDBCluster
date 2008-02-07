package test.domain;

import junit.framework.TestCase;
import mycluster.CBicycle;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.domain.DomainChecker;
import org.jdbcluster.domain.DomainCheckerImpl;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

public class TestDomainList extends TestCase {

	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");
		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.TestDomainCheck.xml");
		//JDBClusterSimpleConfig.setHibernateConfiguration(null, "mapping/hibernate.cfg.xml");
		super.setUp();
	}

	public void testDomainList() {
		DomainChecker dc = DomainCheckerImpl.getInstance();
		ValidDomainEntries<String> domList = dc.getPossibleDomainEntries("ColorTypeDomain");
		assertEquals(4, domList.size());
	}
	
	public void testDomainListPath() {
		DomainChecker dc = DomainCheckerImpl.getInstance();
		ValidDomainEntries<String> domList;
		
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		/*
		 * Test for ColorType "Color"
		 */
		rad.setColorType("Color");
		
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(3, domList.size());
		assertTrue(domList.contains("RED"));
		assertTrue(domList.contains("GREEN"));
		assertTrue(domList.contains("BLUE"));
		assertFalse(domList.isNullAllowed());
		
		/*
		 * set Color to valid value "RED" and test ColorShading
		 * additionalmaster does not match
		 */
		rad.setColor("RED");
		
		domList = dc.getValidDomainEntries(rad, "colorShading");
		assertEquals(2, domList.size());
		assertTrue(domList.contains("LightRED"));
		assertTrue(domList.contains("DarkRED"));
		assertTrue(domList.isNullAllowed());
		
		/*
		 * Test for ColorType "BlackWhite"
		 */
		rad.setColorType("BlackWhite");
		
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(3, domList.size());
		assertTrue(domList.contains("BLACK"));
		assertTrue(domList.contains("WHITE"));
		assertTrue(domList.contains("GREY50%"));
		assertTrue(domList.isNullAllowed());
	}
	
	public void testDomainListAdditionalMaster() {
		DomainChecker dc = DomainCheckerImpl.getInstance();
		ValidDomainEntries<String> domList;
		
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		/*
		 * test defined additional domain for "All" and "RED"
		 * additionalmaster does match
		 */
		rad.setColorType("All");
		rad.setColor("RED");
		
		domList = dc.getValidDomainEntries(rad, "colorShading");
		assertEquals(2, domList.size());
		assertTrue(domList.contains("DarkRED"));
		assertTrue(domList.contains("MiddleRED"));
		assertFalse(domList.isNullAllowed());
		
		/*
		 * test defined additional domain for "Color" and "RED"
		 * additionalmaster does NOT match
		 */
		rad.setColorType("Color");
		domList = dc.getValidDomainEntries(rad, "colorShading");	
		assertEquals(2, domList.size());
		assertTrue(domList.contains("DarkRED"));
		assertTrue(domList.contains("LightRED"));
		assertTrue(domList.isNullAllowed());
		
	}
}
