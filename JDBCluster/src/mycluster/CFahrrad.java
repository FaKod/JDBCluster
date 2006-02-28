package mycluster;

import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.cluster.Cluster;
import dao.Fahrrad;

@DaoLink(dAOClass = Fahrrad.class)
public class CFahrrad extends Cluster {
	
	double durchmesser;
	
	@NoDAO
	double noDAOElement;

	public double getDurchmesser() {
		return durchmesser;
	}

	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}

	public double getNoDAOElement() {
		return noDAOElement;
	}

	public void setNoDAOElement(double noDAOElement) {
		this.noDAOElement = noDAOElement;
	}

}
