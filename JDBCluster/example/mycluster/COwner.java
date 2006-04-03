package mycluster;

import org.jdbcluster.metapersistence.cluster.AssocCluster;

public class COwner extends AssocCluster implements Historical{

	String name;

	boolean historical;

	public boolean getHistorical() {
		return historical;
	}

	public void setHistorical(boolean historical) {
		this.historical = historical;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
