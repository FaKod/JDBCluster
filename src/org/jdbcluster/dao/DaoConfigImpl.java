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
package org.jdbcluster.dao;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

/**
 * 
 * @author Christopher Schmidt
 * reads Dao configuration part
 */
public class DaoConfigImpl extends SAXReader implements DaoConfig {
	
	private String configuration;
	private Document document;

	public String getConfiguration() {
		return configuration;
	}

	public void setConfiguration(String configuration) {
		this.configuration = configuration;
		try {
			document = read(configuration);
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	/**
	 * Constructor which sets the config path
	 * @param config the configuration to be set
	 */
	public DaoConfigImpl(String config) {
		this.setConfiguration(config);
	}

	public String getDaoClass(String daoId) {
		String xPath = "//jdbcluster/daotype/dao[@id='" + daoId + "']";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.valueOf("@class");
		}
	}

	public String getDaoId(String daoClass) {
		String xPath = "//jdbcluster/daotype/dao[@class='" + daoClass + "']";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.valueOf("@id");
		}
	}

	public String[] getPropertyName(String daoId) {
		String xPath = "//jdbcluster/daotype/dao[@id='" + daoId + "']/property";
		List<Node> nodes = document.selectNodes(xPath);
		
		if (nodes == null) {
			return null;
		}
		
		String[] res = new String[nodes.size()];
		int i = 0;
		for(Node n : nodes) {
			res[i++] = n.valueOf("@name");
		}
		return res;
	}

	public String getPropertieValue(String daoId, String Property) {
		String xPath = "//jdbcluster/daotype/dao[@id='" + daoId + "']/property[@name='" + Property + "']/value";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.getStringValue();
		}
	}

	public String getPropertyClass(String daoId, String Property) {
		String xPath = "//jdbcluster/daotype/dao[@id='" + daoId + "']/property[@name='" + Property + "']";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.valueOf("@class");
		}
	}
}
