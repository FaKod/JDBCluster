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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.dom4j.Node;

/**
 * List of configured validation entries
 * @author FaKod
 * @author Christopher Schmidt
 *
 */
final class ValidEntryList extends ArrayList<Valid> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 4798425531880900416L;

	/**
	 * true if it containing valid elements
	 */
	private boolean containsValidElements=false;
	
	/**
	 * true if it containing invalid elements
	 */
	private boolean containsInValidElements=false;
	
	/**
	 * true if it containing additional master elements
	 */
	private boolean containsAddMasterElements=false;
	
	/**
	 * Maps configured domain value entries to Valid entries
	 */
	private HashMap<String, Valid> mapValuesToValid = new HashMap<String, Valid>();
	
	/**
	 * maps AddMasterDomEntryValue -> MasterDomainID -> ValidEntryList
	 */
	private HashMap<String,HashMap<String, ValidEntryList>> addMasterMap;

	/**
	 * Constructs an fills object
	 * @param validEntries List of validation entries (valid entries)
	 * @param invalidEntries List of validation entries (invalid entries)
	 */
	ValidEntryList(List<Node> validEntries, List<Node> invalidEntries, List<Node> addMasterEntries) {
		if(!validEntries.isEmpty()) {
			containsValidElements = true;
			for( Node n : validEntries)
				fill(n, true);
		}
		
		if(!invalidEntries.isEmpty()) {
			containsInValidElements = true;
			for( Node n : invalidEntries)
				fill(n, false);
		}
		
		if(addMasterEntries!=null && addMasterEntries.size()>0) {
			fillAddMasterEntries(addMasterEntries);
			containsAddMasterElements = true;
		}
	}

	/**
	 * parses additional Master Dependancies
	 * @param addMasterEntries 1st level of tags
	 */
	@SuppressWarnings("unchecked")
	private void fillAddMasterEntries(List<Node> addMasterEntries) {
		/**
		 * maps AddMasterDomEntryValue -> MasterDomainID -> ValidEntryList
		 */
		addMasterMap = new HashMap<String,HashMap<String, ValidEntryList>>();
		
		for( Node n : addMasterEntries) {
			String domEntryValue = n.valueOf("@value");
			String domEntryMasterDomain = n.valueOf("@masterdomainid");
			
			HashMap<String, ValidEntryList> masterDomainMap = addMasterMap.get(domEntryValue);
			if(masterDomainMap==null) {
				masterDomainMap = new HashMap<String, ValidEntryList>();
				addMasterMap.put(domEntryValue, masterDomainMap);
			}
			ValidEntryList  validValueList = masterDomainMap.get(domEntryMasterDomain);
			if(validValueList==null) {
				List<Node> validEntries = n.selectNodes("valid");
				List<Node> invalidEntries = n.selectNodes("invalid");
				List<Node> nextAddMasterEntries = n.selectNodes("additionalmaster");
				validValueList = new ValidEntryList(validEntries, invalidEntries, nextAddMasterEntries);
				masterDomainMap.put(domEntryMasterDomain, validValueList);
			}
		}
	}

	/**
	 * Creates Valid objects filles and stores it
	 * @param n validation Node
	 * @param valid Node is a valid or invalid Node
	 */
	private void fill(Node n, boolean valid) {
		Valid v = new Valid();
		v.valid = valid;
		v.all = n.valueOf("@all").equalsIgnoreCase("true");
		v.nullValue = n.valueOf("@null").equalsIgnoreCase("true");
		v.value = n.valueOf("@value");
		if(v.value.length()==0) {
			v.value = null;
			this.add(v);
		}
		else {
			mapValuesToValid.put(v.value, v);
		}
	}
	
	/**
	 * returns Valid entries if they are configured through value tags
	 * @param domainEntry domain entry value
	 * @return Valid objects if available
	 */
	public Valid getValidFromDomainEntry(String domainEntry) {
		return mapValuesToValid.get(domainEntry);
	}
	
	/**
	 * used to get the complete hash map
	 * @return HashMap<String, Valid> maps entry values to Valid instances
	 */
	HashMap<String, Valid> getMapValuesToValid() {
		return mapValuesToValid;
	}
	
	/**
	 * returns Valid entries if they are configured through value tags
	 * @param masterDomainId master domain id as configured
	 * @param masterValue master value from cluster instance property
	 * @return ArrayList<ValidEntryList>
	 */
	public ArrayList<ValidEntryList> getValidFromDomainEntry(String[] masterDomainId, String[] masterValue) {
		ArrayList<ValidEntryList> valid = new ArrayList<ValidEntryList>();
		
		valid.add(this);
		
		recAddMaster(valid, this, masterDomainId, masterValue, 0);
		return valid;
	}
	
	/**
	 * recursive internal method to fill the ArrayList<ValidEntryList> list
	 * @param valid the ArrayList<ValidEntryList> list
	 * @param ved ValidEntryList to use
	 * @param masterDomainId master domain id
	 * @param masterValue master domain value
	 * @param index index in the arrays used per iteration
	 */
	private void recAddMaster (ArrayList<ValidEntryList> valid, ValidEntryList ved, String[] masterDomainId, String[] masterValue, int index) {
		String masterDomID = masterDomainId[index];
		String masterVal = masterValue[index];
		
		if(ved.addMasterMap==null)
			return;
		
		HashMap<String, ValidEntryList> m1 = ved.addMasterMap.get(masterVal);
		if(m1==null)
			return;
		
		ValidEntryList nextVed = m1.get(masterDomID);
		if(nextVed==null)
			return;
		
		valid.add(nextVed);
		
		if(++index==masterDomainId.length)
			return;

		recAddMaster(valid, nextVed, masterDomainId, masterValue, index);
	}
	
	/**
	 * check for additional master elements
	 * @return true if it has some
	 */
	public boolean isContainsAddMasterElements() {
		return containsAddMasterElements;
	}

	/**
	 * check for invalid elements
	 * @return true if it has some
	 */
	public boolean isContainsInValidElements() {
		return containsInValidElements;
	}

	/**
	 * check for valid elements
	 * @return true if it has some
	 */
	public boolean isContainsValidElements() {
		return containsValidElements;
	}

}