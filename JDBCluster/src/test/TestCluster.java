package test;

import junit.framework.TestCase;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

import mycluster.CAuto;
import mycluster.CBesitzer;
import mycluster.CErsatzteile;
import mycluster.CFahrrad;

public class TestCluster extends TestCase {

	public void testAssociations() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("auto");
		CAuto cAuto = ClusterFactory.newInstance(cAutoType);

		CSet<CErsatzteile> ersatzList = cAuto.getCersatzteile();
		CErsatzteile ersatz = new CErsatzteile();

		ersatzList.add(ersatz);
		assertEquals(1, ersatzList.size());

		CErsatzteile ersatz2 = new CErsatzteile();
		ersatzList.add(ersatz2);
		assertEquals(2, ersatzList.size());

		CSet<CErsatzteile> ersatzList2 = cAuto.getCersatzteile();
		assertEquals(2, ersatzList2.size());

		ersatzList.remove(ersatz);
		assertEquals(1, ersatzList.size());

		// cAuto.loadById(100L);
	}

	public void testAttributes() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("auto");
		CAuto cAuto = ClusterFactory.newInstance(cAutoType);
		
		cAuto.setName("Borg");
		cAuto.getName();

		CErsatzteile ersatz = new CErsatzteile();
		CSet<CErsatzteile> ersatzList = cAuto.getCersatzteile();
		ersatzList.add(ersatz);

		ersatz.setLieferant("Picard");
		assertEquals("Picard", ersatz.getLieferant());
	}

	public void testFahrrad() {
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("fahrrad");
		CFahrrad cf = ClusterFactory.newInstance(cFahrradType);

		cf.setDurchmesser(10.0);
		assertEquals(10.0, cf.getDurchmesser());

		cf.setNoDAOElement(11.0);
		assertEquals(11.0, cf.getNoDAOElement());
	}

	public void testHistory() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("auto");
		CAuto cAuto = ClusterFactory.newInstance(cAutoType);
		CSet<CBesitzer> besitzer = cAuto.getCbesitzer();

		for (int i = 0; i < 5; i++)
			besitzer.add(new CBesitzer());

		for (CBesitzer cb : besitzer)
			besitzer.remove(cb);
		
		for (CBesitzer cb : besitzer)
			assertEquals(true, cb.getHistorical());

	}
}
