package mycluster;

import org.jdbcluster.metapersistence.cluster.AssocCluster;

public class CErsatzteile extends AssocCluster{
	String lieferant;

	public String getLieferant() {
		return lieferant;
	}

	public void setLieferant(String lieferant) {
		this.lieferant = lieferant;
	}
}
