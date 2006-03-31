package mycluster.domain;

import org.jdbcluster.domain.DomainList;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.exception.ConfigurationException;

/**
 * Demo for a class which returns valid values for a string domain
 * @author FaKod
 *
 */
public class ColorDomainList implements DomainList {

	static ValidDomainEntries<String> vde = new ValidDomainEntries<String>(true, "RED", "GREEN", "BLUE", "BLACK", "WHITE", "GREY50%");

	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if (domainId.equals("ColorDomain"))
			return vde;
		throw new ConfigurationException("Wrong Domain. Requested: " + domainId + " needed ColorDomain");
	}

}
