package mycluster.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.jdbcluster.domain.DomainList;
import org.jdbcluster.domain.DomainPrivilegeList;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.exception.ConfigurationException;

/**
 * Demo for a class which returns valid values for a string domain
 * @author FaKod
 *
 */
public class ColorTypeDomainList implements DomainList, DomainPrivilegeList {

	static ValidDomainEntries<String> vde = new ValidDomainEntries<String>("Color", "BlackWhite", "None", "All");

	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if (domainId.equals("ColorTypeDomain"))
			return vde;
		throw new ConfigurationException("Wrong Domain. Requested: " + domainId
				+ " needed ColorTypeDomain");
	}
	
	static HashMap<String, HashSet<String>> privDom;
	static {
		privDom = new HashMap<String, HashSet<String>>();
		for(String dom: vde) {
			HashSet<String> hs = new HashSet<String>();
			hs.add("NeedsRight4DomainValue[" + dom + "]");
			privDom.put(dom, hs);
		}
	}

	public Set<String> getDomainEntryPivilegeList(String domainId, String value) {
		return privDom.get(value);
	}

}
