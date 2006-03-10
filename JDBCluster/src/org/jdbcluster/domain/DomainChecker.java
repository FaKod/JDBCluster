/*
 * Copyright 2002-2005 the original author or authors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jdbcluster.domain;

import java.util.HashMap;

import org.jdbcluster.exception.ConfigurationException;
import org.jdbcluster.exception.DomainException;

/**
 * 
 * @author Christopher Schmidt
 * checks slave domains for valid String values
 * 1. valid or invalid tags with value attribute
 * 2. valid tags with all attribute
 * 3. invalid tags with all attribute
 */
public class DomainChecker extends DomainBase {

	static DomainChecker dc;

	/**
	 * the check rules are singleton so this class is singleton
	 * @return
	 */
	public static DomainChecker getDomainChecker() {
		if (dc == null)
			dc = new DomainChecker();
		return dc;
	}

	/**
	 * Main method for domain checking
	 * @param masterDomainId configured master domain id (fe. "ColorTypeDomain") 
	 * @param masterValue the master entry (fe. "Color", "BlackWhite")
	 * @param SlaveDomainId configured slave domain id (fe. "ColorDomain")
	 * @param slaveValue the slave entry (fe. "RED", "BLACK")
	 * @return true if the slave value is allowed. Otherwise false (quel surprise) 
	 */
	public boolean check(String masterDomainId, String masterValue, String SlaveDomainId, String slaveValue) {
		EntrySet es = getDomainConfig().getEntrySet(masterDomainId);
		if (es == null)
			throw new ConfigurationException("master domain [" + masterDomainId + "] annotated [@Domain] but not configured");

		HashMap<String, ValidEntryList> slaveEntries = es.get(masterValue);
		if (slaveEntries == null) {
			if(es.get("*")==null)
				throw new DomainException("master domain [" + masterDomainId + "]: value [" + masterValue + "] is not configured");
			else
				return true; // wildcard match
		}
		return validate(slaveEntries.get(SlaveDomainId), slaveValue);
	}

	/**
	 * Check algorithmus
	 * 1. valid or invalid tags with value attribute
	 * 2. valid tags with all attribute
	 * 3. invalid tags with all attribute
	 * @param valid configured ValidEntryList
	 * @param slaveValue slave value to check
	 * @return true if its allowed
	 */
	private boolean validate(ValidEntryList valid, String slaveValue) {
		Valid validDomEntry = valid.getValidFromDomainEntry(slaveValue);
		if (validDomEntry != null) {
			return validDomEntry.valid;
		}

		for (Valid v : valid) {
			if (v.value == null) { // case !=null is handled through
									// getValidFromDomainEntry...
				if (v.valid) { // is it a valid tag or an invalid tag
					if (v.all) {
						if (!v.nullValue) { // check if null value is allowed
							return !(slaveValue == null);
						}
						return true;
					}
				} else { // its a invalid tag
					if (v.all) {
						if (!v.nullValue) { // check if null value is allowed
							return slaveValue == null;
						}
						return false;
					}
				}
			}
		}
		return false;
	}

}
