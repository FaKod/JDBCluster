package test.domain.adddomain;

import junit.framework.TestCase;

import org.apache.log4j.PropertyConfigurator;
import org.jdbcluster.JDBClusterSimpleConfig;
import org.jdbcluster.exception.DomainException;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;


public class TestAddMasterDomain extends TestCase {

	protected void setUp() throws Exception {
		//	 configuring logging
		PropertyConfigurator.configure("xml/logging.properties");
		
		JDBClusterSimpleConfig.setConfiguration("test/test/domain/adddomain/DomainConfig.xml");
		
		
	}

	protected void tearDown() throws Exception {
	}
	
	/*
	categoryCode		subCatCode		subSubCatCode		subSubSubCatCode		
	[null]					[null]				[null]					[null]					
	________________________________________________________________________________
	A1							[null]				[any],[null]		[null]
									____________________________________________________________
									A2						[any],[null]		A4,[null]
									____________________________________________________________
									B2						A3,[null]				[any],[null]
	________________________________________________________________________________
	B1							[null]				[any],[null]		[null]							
									____________________________________________________________
									A2						[any],[null]		A4,[null]
									____________________________________________________________
									B2						B3,[null]				B4,[null]
	*/
	
	public void testInValidDomains() {
		AddDomainCluster cluster = ClusterFactory.newInstance("AddDomainCluster");
		try {
			cluster.setCodes("A1", null, null, "A4");
			fail("invalid catcodes A1, null, null, A4");
		} catch (DomainException ex) {}
		try {
			cluster.setCodes("A1", "A2", null, "B4");
			fail("invalid catcodes A2, null, null, B4");
		} catch (DomainException ex) {}
		try {
			cluster.setCodes("B1", "B2", "B3", "A4");
			fail("invalid catcodes B1, B2, B3, A4");
		} catch (DomainException ex) {}
		try {
			cluster.setCodes("B1", "A2", "B3", "B4");
			fail("invalid catcodes B1, A2, B3, B4");
		} catch (DomainException ex) {}
		
	}
	
	public void testValidDomains() {
		AddDomainCluster cluster = ClusterFactory.newInstance("AddDomainCluster");
		try {
			cluster.setCodes(null, null, null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("setCodes(null, null, null, null) not possible");
		}
		try {
			cluster.setCodes("A1", null, null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", null, "A3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", null, "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", null, "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", "A3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", "A3", "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "A2", "B3", "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", null, "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", null, "B4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", "A3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", "A3", "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("A1", "B2", "A3", "B4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", null, null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", null, "A3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", null, "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", null, "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", "A3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", "A3", "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "A2", "B3", "A4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", null, null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", null, "B4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", "B3", "B4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", "B3", null);
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}
		try {
			cluster.setCodes("B1", "B2", "B3", "B4");
		} catch (DomainException ex) {
			ex.printStackTrace();
			fail("");
		}		
		try {
			cluster.setCodes("B1", "B2", "B3", "AA4");
			fail("AA4 should not be possible");
		} catch (DomainException ex) {
			ex.printStackTrace();		
		}		
	}

}
