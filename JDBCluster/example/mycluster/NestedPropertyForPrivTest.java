package mycluster;

import org.jdbcluster.metapersistence.annotation.Domain;

public class NestedPropertyForPrivTest {
	
	@Domain(domainId="ColorTypeDomain")
	String nestedDomain;

	public String getNestedDomain() {
		return nestedDomain;
	}

	public void setNestedDomain(String nestedDomain) {
		this.nestedDomain = nestedDomain;
	}

}
