package mycluster;

import java.util.ArrayList;
import java.util.List;

import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.cluster.CSet;

import dao.Car;
import dao.Owner;
import dao.SparePart;

@DaoLink(dAOClass = Car.class)
public class CCar extends CVehicle {

	//DAO Field name
	private String name;

	//DAO assoc to Ersatzteile
	@DaoLink(dAOClass = SparePart.class)
	CSet<CSparePart> csparepart;
	
	//DAO assoc to Besitzer
	@DaoLink(dAOClass = Owner.class)
	MyRemoveSpecialTreatmentCSet<COwner> cbesitzer;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public Iterable<CSparePart> getSpareParts() {
		List<CSparePart> result = new ArrayList<CSparePart>();
		for (SparePart part : ((Car)getDao()).getSparePart()) {
			CSparePart newPart = new CSparePart();
			newPart.setDao(part);
			result.add(newPart);
		}
		return result;
	}

	public CSet<CSparePart> getCsparepart() {
		return csparepart;
	}

	public void setCsparepart(CSet<CSparePart> cersatzteile) {
		this.csparepart = cersatzteile;
	}

	public Iterable<COwner> getOwner() {
		List<COwner> result = new ArrayList<COwner>();
		for (Owner owner : ((Car)getDao()).getBesitzer()) {
			COwner newOwner = new COwner();
			newOwner.setDao(owner);
			result.add(newOwner);
		}
		return result;
	}
	
	public MyRemoveSpecialTreatmentCSet<COwner> getCbesitzer() {
		return cbesitzer;
	}	

	@SuppressWarnings("unchecked")
	public void setCbesitzer(MyRemoveSpecialTreatmentCSet<COwner> cbesitzer) {
		this.cbesitzer = (MyRemoveSpecialTreatmentCSet)cbesitzer;
	}
}
