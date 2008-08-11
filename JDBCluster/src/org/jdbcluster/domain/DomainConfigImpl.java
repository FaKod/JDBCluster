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
import java.util.List;

import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.jdbcluster.JDBClusterConfigurationBase;
import org.jdbcluster.exception.ConfigurationException;

/**
 * parses Domain Dependancy Configuration
 * @author Christopher Schmidt
 *
 */
public class DomainConfigImpl extends JDBClusterConfigurationBase implements DomainConfig {
	
	/**
	 * holds all entries of Domain Dependancy Configuration
	 */
	HashMap<String, EntrySet> dependancies;

	/**
	 * create "emty" Object
	 *
	 */
	public DomainConfigImpl() {
	}

	/**
	 * calls setConfiguration direcly 
	 * @param config
	 */
	public DomainConfigImpl(String config) {
		this.setConfiguration(config);
	}

	/**
	 * retreive Configuration
	 * @return path to configuration xml file
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * starts parsing process of entries in 
	 * configuration xml file
	 * @param configuration
	 */
	@SuppressWarnings("unchecked")
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
		dependancies = new HashMap<String, EntrySet>();
		try {
			document = read(configuration);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new ConfigurationException("Error in DomainConfig File", e);
		}
		
		String xPathDomains = "//jdbcluster/domaindependancy/domain";
		List<Node> domainNodes = document.selectNodes(xPathDomains);
		if(domainNodes==null)
			return;
		
		// iterate over all configured "domain" elements
		for(Node domainNode : domainNodes) {
			String domainId = domainNode.valueOf("@domainid");
			List<Node> domEntryNodes = domainNode.selectNodes("entry");
			EntrySet es = new EntrySet(domainId, domEntryNodes);
			dependancies.put(domainId,es);
		}	
	}

	public EntrySet getEntrySet(String domainId) {
		return dependancies.get(domainId);
	}
	
	public String getDomainListClass(String domainId) {
		String xPathDomain = "//jdbcluster/domaindependancy/domain[@domainid='" + domainId + "']";
		Node node = document.selectSingleNode(xPathDomain);

		if (node == null) {
			return null;
		} 
		return node.valueOf("@domainlistclass");
	}
}
