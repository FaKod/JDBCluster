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

public class TestDomainList2 extends TestCase {
	@Override
	protected void setUp() throws Exception {
		// configuring logging
		PropertyConfigurator.configure("xml/logging.properties");
		JDBClusterSimpleConfig.setConfiguration("test/test/domain/jdbcluster.conf.TestDomainCheck2.xml");
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
		rad.setColorType("Color");
		
		/**
		 * testing valid tags and a valid tag which is not in the
		 * set of possible values
		 */
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(3, domList.size());
		assertTrue(domList.contains("RED"));
		assertTrue(domList.contains("GREEN"));
		assertTrue(domList.contains("BLUE"));
		assertFalse(domList.contains("GREY"));
		assertFalse(domList.isNullAllowed());
		
		/**
		 * testing invalid tag for value "BLACK"
		 */
		rad.setColorType("BlackWhite");
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(5, domList.size());
		assertTrue(domList.contains("RED"));
		assertTrue(domList.contains("GREEN"));
		assertTrue(domList.contains("BLUE"));
		assertTrue(domList.contains("WHITE"));
		assertTrue(domList.contains("GREY50%"));
		assertTrue(domList.isNullAllowed());
		assertFalse(domList.contains("BLACK"));	
	
		/**
		 * checking for <invalid all="true"/> tag
		 */
		rad.setColorType("None");
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(0, domList.size());
		assertTrue(domList.isNullAllowed());
		
		/**
		 * checking <valid all="true" null="true"/> tag
		 */
		rad.setColorType("All");
		domList = dc.getValidDomainEntries(rad, "color");
		assertEquals(6, domList.size());
		assertTrue(domList.contains("RED"));
		assertTrue(domList.contains("GREEN"));
		assertTrue(domList.contains("BLUE"));
		assertTrue(domList.contains("WHITE"));
		assertTrue(domList.contains("GREY50%"));
		assertTrue(domList.contains("BLACK"));	
		assertTrue(domList.isNullAllowed());
	}
	
	public void testDomainListAdditionalMaster() {
		DomainChecker dc = DomainCheckerImpl.getInstance();
		ValidDomainEntries<String> domList;
		
		// we use cluster auto now
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");

		// create a Cluster and persist it
		CBicycle rad = ClusterFactory.newInstance(cFahrradType);
		
		rad.setColorType("All");
		rad.setColor("RED");
		
		domList = dc.getValidDomainEntries(rad, "colorShading");
		assertEquals(2, domList.size());
		assertTrue(domList.contains("DarkRED"));
		assertTrue(domList.contains("MiddleRED"));
		assertFalse(domList.contains("LightRED"));
		
		rad.setColorType("Color");
		domList = dc.getValidDomainEntries(rad, "colorShading");	
		assertEquals(2, domList.size());
		assertTrue(domList.contains("DarkRED"));
		assertTrue(domList.contains("LightRED"));
		assertFalse(domList.contains("MiddleRED"));
		
	}
}
