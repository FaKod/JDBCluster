package mycluster;

import org.jdbcluster.metapersistence.cluster.CSet;

public interface ICar extends IVehicle {

	String getName();

	void setName(String name);

	Iterable<CSparePart> getSpareParts();

	CSet<CSparePart> getCsparepart();

	void setCsparepart(CSet<CSparePart> cersatzteile);

	Iterable<COwner> getOwner();

	MyRemoveSpecialTreatmentCSet<COwner> getCbesitzer();

	@SuppressWarnings("unchecked")
	void setCbesitzer(MyRemoveSpecialTreatmentCSet<COwner> cbesitzer);

}