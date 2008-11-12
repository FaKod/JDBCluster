package test.domain.adddomain;

import org.jdbcluster.metapersistence.annotation.AddDomainDependancy;
import org.jdbcluster.metapersistence.annotation.DaoLink;
import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.DomainDependancy;
import org.jdbcluster.metapersistence.annotation.NoDAO;
import org.jdbcluster.metapersistence.cluster.Cluster;

import dao.Bicycle;


@DaoLink(dAOClass = Bicycle.class) // Bycycle just used as a dummy
public class AddDomainCluster extends Cluster {
	
	@NoDAO
	@Domain(domainId="categoryCode")
	private String categoryCode;

	@NoDAO
	@DomainDependancy(domainId ="subCatCode", dependsFromProperty="categoryCode")
	private String subCatCode;

	@NoDAO
	@DomainDependancy(domainId = "subSubCatCode", dependsFromProperty = "subCatCode")
	@AddDomainDependancy(addDepFromProp = {	"categoryCode" })
	private String subSubCatCode;

	@NoDAO
	@DomainDependancy(domainId = "subSubSubCatCode", dependsFromProperty = "subSubCatCode")
	@AddDomainDependancy(addDepFromProp = { "categoryCode", "subCatCode" })
	private String subSubSubCatCode;

	public String getCategoryCode() {
		return categoryCode;
	}

	public void setCategoryCode(String categoryCode) {
		this.categoryCode = categoryCode;
	}

	public String getSubCatCode() {
		return subCatCode;
	}

	public void setSubCatCode(String subCatCode) {
		this.subCatCode = subCatCode;
	}

	public String getSubSubCatCode() {
		return subSubCatCode;
	}

	public void setSubSubCatCode(String subSubCatCode) {
		this.subSubCatCode = subSubCatCode;
	}

	public String getSubSubSubCatCode() {
		return subSubSubCatCode;
	}

	public void setSubSubSubCatCode(String subSubSubCatCode) {
		this.subSubSubCatCode = subSubSubCatCode;
	}
	
	public void setCodes(String catCode, String subCatCode, String subSubCatCode, String subSubSubCatCode) {
		setCategoryCode(catCode);
		setSubCatCode(subCatCode);
		setSubSubCatCode(subSubCatCode);
		setSubSubSubCatCode(subSubSubCatCode);
	}

}
