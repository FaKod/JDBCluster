package mycluster;

import org.jdbcluster.metapersistence.annotation.AddDomainDependancy;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.annotation.NoPrivilegeCheck;
import org.jdbcluster.metapersistence.annotation.PrivilegesCluster;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;
import org.jdbcluster.metapersistence.annotation.PrivilegesMethod;
import org.jdbcluster.metapersistence.cluster.Cluster;
import org.jdbcluster.privilege.PrivilegedCluster;

import dao.Bicycle;

@PrivilegesCluster(required={"BIKE"},property={"colorType", "nested.nestedDomain"})
@DaoLink(dAOClass = Bicycle.class)
public class CBicycle extends Cluster implements PrivilegedCluster {
	
	@PrivilegesDomain
	@Domain(domainId="ColorTypeDomain")
	String colorType;
	
	@DomainDependancy(domainId="ColorDomain", dependsFromProperty="colorType")
	String color;
	
	@DomainDependancy(domainId="ColorShadingDomain", dependsFromProperty="color")
	@AddDomainDependancy(addDepFromProp={"colorType"})
	String colorShading;
	
	double durchmesser;
	
	@NoDAO
	double noDAOElement;
	
	@NoDAO
	NestedPropertyForPrivTest nested;

	@PrivilegesMethod(required={"GETNESTED"})
	public NestedPropertyForPrivTest getNested() {
		return nested;
	}

	public void setNested(NestedPropertyForPrivTest nested) {
		this.nested = nested;
	}

	public String getColorShading() {
		return colorShading;
	}

	@PrivilegesMethod(required={"SETCOLSHADE"})
	public void setColorShading(String colorShading) {
		this.colorShading = colorShading;
	}
	
	@NoPrivilegeCheck
	public double getDurchmesser() {
		return durchmesser;
	}

	@NoPrivilegeCheck
	public void setDurchmesser(double durchmesser) {
		this.durchmesser = durchmesser;
	}

	public double getNoDAOElement() {
		return noDAOElement;
	}

	public void setNoDAOElement(double noDAOElement) {
		this.noDAOElement = noDAOElement;
	}

	@PrivilegesMethod(required={"GETCOLOR"})
	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}

	@PrivilegesMethod(required={"GETCOLTYPE"})
	public String getColorType() {
		return colorType;
	}

	public void setColorType(String colorType) {
		this.colorType = colorType;
	}

}
