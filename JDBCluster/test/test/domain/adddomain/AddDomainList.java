package test.domain.adddomain;

import org.jdbcluster.domain.DomainList;
import org.jdbcluster.domain.ValidDomainEntries;
import org.jdbcluster.exception.ConfigurationException;

public class AddDomainList implements DomainList {

	private static final ValidDomainEntries<String> CATCODES = new ValidDomainEntries<String>(null, "A1", "B1");
	private static final ValidDomainEntries<String> SUBCATCODES = new ValidDomainEntries<String>(null, "A2", "B2");
	private static final ValidDomainEntries<String> SUBSUBCATCODES = new ValidDomainEntries<String>(null, "A3", "B3");
	private static final ValidDomainEntries<String> SUBSUBSUBCATCODES = new ValidDomainEntries<String>(null, "A4", "B4", "AA4");
	
	public ValidDomainEntries<String> getDomainEntryList(String domainId) {
		if ("categoryCode".equals(domainId)) {
			return CATCODES;
		} else if ("subCatCode".equals(domainId)) {
			return SUBCATCODES;
		} else if ("subSubCatCode".equals(domainId)) {
			return SUBSUBCATCODES;
		} else if ("subSubSubCatCode".equals(domainId)) {
			return SUBSUBSUBCATCODES;
		} else {
			throw new ConfigurationException("Wrong domainId " + domainId);
		}
	}

}
