package org.jdbcluster.dao;

import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

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
