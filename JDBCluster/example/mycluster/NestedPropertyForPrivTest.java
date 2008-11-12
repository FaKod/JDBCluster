package mycluster;

import org.jdbcluster.metapersistence.annotation.Domain;
import org.jdbcluster.metapersistence.annotation.PrivilegesDomain;

public class NestedPropertyForPrivTest {
	
	@PrivilegesDomain
	@Domain(domainId="NestedDomain")
	String nestedDomain;

	public String getNestedDomain() {
		return nestedDomain;
	}

	public void setNestedDomain(String nestedDomain) {
		this.nestedDomain = nestedDomain;
	}

}
