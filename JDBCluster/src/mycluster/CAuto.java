package mycluster;

import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.CSet;
import org.jdbcluster.metapersistence.cluster.Cluster;
import dao.Auto;
import dao.Besitzer;
import dao.Ersatzteile;

@DaoLink(dAOClass = Auto.class)
public class CAuto extends Cluster {

	//DAO Field name
	private String name;

	//DAO assoc to Ersatzteile
	@DaoLink(dAOClass = Ersatzteile.class)
	CSet<CErsatzteile> cersatzteile;
	
	//DAO assoc to Besitzer
	@DaoLink(dAOClass = Besitzer.class)
	MyRemoveSpecialTreatmentCSet<CBesitzer> cbesitzer;

	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CSet<CErsatzteile> getCersatzteile() {
		return cersatzteile;
	}

	public void setCersatzteile(CSet<CErsatzteile> cersatzteile) {
		this.cersatzteile = cersatzteile;
	}

	public MyRemoveSpecialTreatmentCSet<CBesitzer> getCbesitzer() {
		return cbesitzer;
	}

	public void setCbesitzer(MyRemoveSpecialTreatmentCSet<CBesitzer> cbesitzer) {
		this.cbesitzer = (MyRemoveSpecialTreatmentCSet)cbesitzer;
	}
}
