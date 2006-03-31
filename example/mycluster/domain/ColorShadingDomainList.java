package mycluster.domain;

import org.jdbcluster.domain.DomainList;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.exception.ConfigurationException;

/**
 * Demo for a class which returns valid values for a string domain
 * @author FaKod
 *
 */
public class ColorShadingDomainList implements DomainList {

	static ValidDomainEntries<String> vde = new ValidDomainEntries<String>(true, "LightRED", "MiddleRED", "DarkRED");

	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if (domainId.equals("ColorShadingDomain"))
			return vde;
		throw new ConfigurationException("Wrong Domain. Requested: " + domainId
				+ " needed ColorShadingDomain");
	}

}
