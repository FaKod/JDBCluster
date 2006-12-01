package test;

import junit.framework.TestCase;

import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.clustertype.ClusterTypeFactory;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.ClusterFactory;

import mycluster.CCar;
import mycluster.COwner;
import mycluster.CSparePart;
import mycluster.CBicycle;

public class TestCluster extends TestCase {

	public void testAssociations() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		CCar cAuto = ClusterFactory.newInstance(cAutoType);

		CSet<CSparePart> ersatzList = cAuto.getCsparepart();
		CSparePart ersatz = new CSparePart();

		ersatzList.add(ersatz);
		assertEquals(1, ersatzList.size());

		CSparePart ersatz2 = new CSparePart();
		ersatzList.add(ersatz2);
		assertEquals(2, ersatzList.size());

		CSet<CSparePart> ersatzList2 = cAuto.getCsparepart();
		assertEquals(2, ersatzList2.size());

		ersatzList.remove(ersatz);
		assertEquals(1, ersatzList.size());

		// cAuto.loadById(100L);
	}

	public void testAttributes() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		CCar cAuto = ClusterFactory.newInstance(cAutoType);
		
		cAuto.setName("Borg");
		cAuto.getName();

		CSparePart ersatz = new CSparePart();
		CSet<CSparePart> ersatzList = cAuto.getCsparepart();
		ersatzList.add(ersatz);

		ersatz.setLieferant("Picard");
		assertEquals("Picard", ersatz.getLieferant());
	}

	public void testFahrrad() {
		ClusterType cFahrradType = ClusterTypeFactory.newInstance("bicycle");
		CBicycle cf = ClusterFactory.newInstance(cFahrradType);

		cf.setDurchmesser(10.0);
		assertEquals(10.0, cf.getDurchmesser());

		cf.setNoDAOElement(11.0);
		assertEquals(11.0, cf.getNoDAOElement());
	}

	public void testHistory() {
		ClusterType cAutoType = ClusterTypeFactory.newInstance("car");
		CCar cAuto = ClusterFactory.newInstance(cAutoType);
		CSet<COwner> besitzer = cAuto.getCbesitzer();

		for (int i = 0; i < 5; i++)
			besitzer.add(new COwner());

		for (COwner cb : besitzer)
			besitzer.remove(cb);
		
		for (COwner cb : besitzer)
			assertEquals(true, cb.getHistorical());

	}
}
