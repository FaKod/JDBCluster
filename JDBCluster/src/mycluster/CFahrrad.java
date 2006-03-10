package mycluster;

import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.cluster.Cluster;
import dao.Fahrrad;

@DaoLink(dAOClass = Fahrrad.class)
public class CFahrrad extends Cluster {
	
	@Domain(domainId="ColorTypeDomain")
	String colorType;
	
	@DomainDependancy(domainId="ColorDomain", dependsFromProperty="colorType")
	String color;
	
	@DomainDependancy(domainId="ColorShadingDomain", dependsFromProperty="color")
	String colorShading;
	
	double durchmesser;
	
	@NoDAO
	double noDAOElement;

	public String getColorShading() {
		return colorShading;
	}

	public void setColorShading(String colorShading) {
		this.colorShading = colorShading;
	}

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

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	public String getColorType() {
		return colorType;
	}

	public void setColorType(String colorType) {
		this.colorType = colorType;
	}

}
