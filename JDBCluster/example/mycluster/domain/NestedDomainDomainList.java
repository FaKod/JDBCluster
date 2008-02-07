package mycluster.domain;

import java.util.HashSet;
import java.util.Set;

import org.jdbcluster.domain.DomainList;
import org.jdbcluster.domain.DomainPrivilegeList;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.exception.ConfigurationException;

public class NestedDomainDomainList implements DomainList, DomainPrivilegeList {
	
	static ValidDomainEntries<String> vde = new ValidDomainEntries<String>("TEST");

	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if (domainId.equals("NestedDomain"))
			return vde;
		throw new ConfigurationException("Wrong Domain. Requested: " + domainId
				+ " needed NestedDomain");
	}
	
	static HashSet<String> privDom;
	static {
		privDom = new HashSet<String>();
		privDom.add("NestedPRIV1");
		privDom.add("NestedPRIV2");
		privDom.add("NestedPRIV3");
	}

	public Set<String> getDomainEntryPivilegeList(String domainId, String value) {
		return privDom;
	}

}
