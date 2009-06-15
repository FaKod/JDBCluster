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
package org.jdbcluster.filter;

import java.util.HashMap;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.jdbcluster.JDBClusterConfigurationBase;
import org.jdbcluster.clustertype.ClusterType;
import org.jdbcluster.exception.CCFilterException;


/**
 * class ClusterSelectImpl keeps the location of the config file
 * "selects.xml" and is responsible for executing the queries on 
 * that file
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 *
 */

public class ClusterSelectImpl extends JDBClusterConfigurationBase implements ClusterSelect {
		
	/**
	 * Constructor which sets the config file
	 * @param config the file to be set
	 */
	public ClusterSelectImpl(String config) {
		this.setConfiguration(config);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void addExtension(String extensionPath) {
		try {
			Document doc = read(extensionPath);
			String xPathClusterType = "//jdbcluster/clustertype";
			String xPath = "//jdbcluster/clustertype/cluster";
			String xPathExtension = "//clustertype/cluster";
						
			List<Element> nodes = doc.selectNodes(xPathExtension);
			List<Element> rootNodes = document.selectNodes(xPath);
			Element clusterType = (Element) document.selectSingleNode(xPathClusterType);
			
			for (Element node : nodes) {
				for (Element rootNode : rootNodes) {
					String clusterExtensionName = node.valueOf("@id");
					String clusterRootName = rootNode.valueOf("@id");
					String nodeExistenceXPath = "//jdbcluster/clustertype/cluster[@id='" + clusterExtensionName + "']";
					Node node2 = document.selectSingleNode(nodeExistenceXPath);
					if (node2== null) {
						node.detach();
						clusterType.add(node);
						break;
					} else if (clusterRootName.equals(clusterExtensionName)) {
						String xPathFilter = "//jdbcluster/clustertype/cluster[@id='" + clusterRootName + "']" + "/select";
						String xPathFilterExtensions = "//clustertype/cluster[@id='" + clusterRootName + "']" + "/select";
						List<Element> extensionFilterNodes = doc.selectNodes(xPathFilterExtensions);
						List<Element> filterNodes = document.selectNodes(xPathFilter);
						for (Element extensionFilter : extensionFilterNodes) {
							for (Element rootFilter : filterNodes) {
								if (extensionFilter.valueOf("@id").equals(rootFilter.valueOf("@id"))) {
									rootNode.remove(rootFilter);
								} 
							}
							extensionFilter.detach();
							rootNode.add(extensionFilter);
						}
						break;
					}
				}
			}
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	
	/**
	* creates a select string on "selects.xml"
	* @param clusterType identifies the ClusterType
	* @param SelectID selects the SelectID
	* @return String
	*/
	public String getWhere(ClusterType clusterType, String SelectID) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null)
			throw new CCFilterException("cannot find filter configuration for ClusterID ["+clusterId+"] and select Id ["+SelectID+"]");
	
		return node.valueOf("@hql");	
	}
	
	/**
	 * returns the alias setting
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getAlias(ClusterType clusterType, String SelectID) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) 
			throw new CCFilterException("cannot find filter configuration for ClusterID ["+clusterId+"] and select Id ["+SelectID+"]");
		
		return node.valueOf("@alias");	
	}
	
	/**
	 * returns the alias setting
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getExt(ClusterType clusterType, String SelectID) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) 
			throw new CCFilterException("cannot find filter configuration for ClusterID ["+clusterId+"] and select Id ["+SelectID+"]");
		
		return node.valueOf("@ext");	
	}
	
	/**
	 * order By statement part
	 * @param clusterType identifies the ClusterType
	 * @param selId selects the SelectID
	 * @return String
	 */
	public String getOrderBy(ClusterType clusterType, String SelectID) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null)
			throw new CCFilterException("cannot find filter configuration for ClusterID ["+clusterId+"] and select Id ["+SelectID+"]");
			
		return node.valueOf("@orderby");	
	}
	
	/**
	 * gets the classname from "selects.xml"
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getClassName(ClusterType clusterType, String SelectID){
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']" + "/FilterClass";

		Node node = document.selectSingleNode(xPath);
		
		if (node == null) 
			throw new CCFilterException("cannot find filter configuration for ClusterID ["+clusterId+"] and select Id ["+SelectID+"]");
		
		return node.valueOf("@class");	
	}
	
	/**
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return all fetched attributes or an empty String
	 */
	@SuppressWarnings("unchecked")
	public String getFetch(ClusterType clusterType, String SelectID) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']" + "/fetch";

		List<Node> nodes = document.selectNodes(xPath); // List content instanceof Node!
		
		String alias = getAlias(clusterType, SelectID);
		if (alias == null || alias.length()==0) {
			alias = "";
		} else {
			alias = alias + ".";
		}
		StringBuilder builder = new StringBuilder();
		for (Node node : nodes) {
			builder.append(' ').append(node.valueOf("@joinType")).append(" join fetch ").append(alias).append(node.valueOf("@property"));
		}
		return builder.toString();
	}

	/**
	 * gets the configuration path of "selects.xml"
	 * @return String
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * sets the configuration path of "selects.xml"
	 * @param configuration
	 */
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
	 * gets the bindings for the specified classname
	 * @param ct identifies the ClusterType
	 * @param selID selects the SelectID
	 * @param className specifies the classname
	 * @return HashMap<String, String>
	 */
	public HashMap<String, String> getBinding(ClusterType ct, String selID, String className) {
		//get the name e.g. unit
		String clusterId = ct.getName();
		//xPath expression to get the classname
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + selID + "']" + "/FilterClass[@class='" + className + "']" + "/binding";

		List<?> list = document.selectNodes(xPath);
		
		if (list.isEmpty()) {
			return null;
		} else {
			HashMap<String, String> binding = new HashMap<String, String>();
			for (int i = 0; i < list.size();i++) {
				binding.put(((Node)list.get(i)).valueOf("@var"), ((Node)list.get(i)).valueOf("@attribute"));
			}
//			returns the attributes, mapped to keys
			return binding;
		}
	}

	/**
	 * gets static part of a hql query read from a String attribute
	 * @param clusterType identifies the ClusterType
	 * @param SelectID the SelectID
	 * @param className specifies the classname
	 * @return String
	 */
	public String getStaticStatementAttribute(ClusterType clusterType, String SelectID, String className) {
		String clusterId = clusterType.getName();
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']" + "/FilterClass[@class='" + className + "']" + "/static";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) 
			return null;
		return node.valueOf("@statementAttribute");	
	}

}
