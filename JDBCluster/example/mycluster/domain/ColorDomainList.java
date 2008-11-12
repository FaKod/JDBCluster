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
public class ColorDomainList implements DomainList, DomainPrivilegeList {

	static ValidDomainEntries<String> vde = new ValidDomainEntries<String>(true, "RED", "GREEN", "BLUE", "BLACK", "WHITE", "GREY50%");

	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if (domainId.equals("ColorDomain"))
			return vde;
		throw new ConfigurationException("Wrong Domain. Requested: " + domainId + " needed ColorDomain");
	}
	
	static HashMap<String, HashSet<String>> privDom;
	static {
		privDom = new HashMap<String, HashSet<String>>();
		for(String dom: vde) {
			HashSet<String> hs = new HashSet<String>();
			hs.add("NeedsRight4[Color]DomainValue[" + dom + "]");
			privDom.put(dom, hs);
		}
		HashSet<String> hs = new HashSet<String>();
		hs.add("NeedsRight4[Color]DomainValue[NULL]");
		privDom.put("NULL", hs);
	}

	public Set<String> getDomainEntryPivilegeList(String domainId, String value) {
		return privDom.get(value==null?"NULL":value);
	}

}
