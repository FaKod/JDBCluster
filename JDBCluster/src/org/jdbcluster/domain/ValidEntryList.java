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
 * @author Christopher Schmidt
 *
 */
final class ValidEntryList extends ArrayList<Valid> {
	
	/**
	 * Maps configured domain value entries to Valid entries
	 */
	private HashMap<String, Valid> mapValuesToValid = new HashMap<String, Valid>();

	/**
	 * Constructs an fills object
	 * @param validEntries List of validation entries (valid entries)
	 * @param invalidEntries List of validation entries (invalid entries)
	 */
	ValidEntryList(List<Node> validEntries, List<Node> invalidEntries) {
		for( Node n : validEntries) {
			fill(n, true);
		}
		
		for( Node n : invalidEntries) {
			fill(n, false);
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
		v.value = n.valueOf("@value");
		if(v.value.length()==0)
			v.value = null;
		else {
			mapValuesToValid.put(v.value, v);
		}
		v.all = n.valueOf("@all").equalsIgnoreCase("true");
		v.nullValue = n.valueOf("@null").equalsIgnoreCase("true");
		this.add(v);
	}
	
	/**
	 * returns Valid entries if they are configured through value tags
	 * @param domainEntry domain entry value
	 * @return Valid objects if available
	 */
	public Valid getValidFromDomainEntry(String domainEntry) {
		return mapValuesToValid.get(domainEntry);
	}
}