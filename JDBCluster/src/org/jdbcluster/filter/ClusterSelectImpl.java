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
import org.dom4j.Node;
import org.dom4j.io.SAXReader;
import org.jdbcluster.clustertype.ClusterType;


/**
 * class ClusterSelectImpl keeps the location of the config file
 * "selects.xml" and is responsible for executing the queries on 
 * that file
 * 
 * @author Philipp Noggler
 * @author Christopher Schmidt
 *
 */

public class ClusterSelectImpl extends SAXReader implements ClusterSelect {

	//path to config file
	private String configuration;
	private Document document;
	
	/**
	 * private Default constructor
	 *
	 */
	private ClusterSelectImpl() {}
		
	/**
	 * Constructor which sets the config file
	 * @param config the file to be set
	 */
	public ClusterSelectImpl(String config) {
		this.setConfiguration(config);
	}
	
	/**
	* creates a select string on "selects.xml"
	* @param clusterType identifies the ClusterType
	* @param SelectID selects the SelectID
	* @return String
	*/
	public String getWhere(ClusterType clusterType, String SelectID) {
		//get the name e.g. unit
		String clusterId = clusterType.getName();
		//xPath expression to get the select statement
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			//returns the select String
			return node.valueOf("@hql");	
		}
	}
	
	/**
	 * returns the alias setting
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getAlias(ClusterType clusterType, String SelectID) {
//		get the name e.g. unit
		String clusterId = clusterType.getName();
		//xPath expression to get the select statement
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			//returns the select String
			return node.valueOf("@alias");	
		}
	}
	
	/**
	 * returns the alias setting
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getExt(ClusterType clusterType, String SelectID) {
//		get the name e.g. unit
		String clusterId = clusterType.getName();
		//xPath expression to get the select statement
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']";
		
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			//returns the select String
			return node.valueOf("@ext");	
		}
	}
	
	/**
	 * gets the classname from "selects.xml"
	 * @param clusterType identifies the ClusterType
	 * @param SelectID selects the SelectID
	 * @return String
	 */
	public String getClassName(ClusterType clusterType, String SelectID){
		//get the name e.g. unit
		String clusterId = clusterType.getName();
		//xPath expression to get the classname
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterId + "']" + "/select[@id='" + SelectID + "']" + "/FilterClass";

		Node node = document.selectSingleNode(xPath);
		
		if (node == null) {
			return null;
		} else {
			//returns the classname as a String
			return node.valueOf("@class");	
		}
		
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

		List list = document.selectNodes(xPath);
		
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

		if (node == null) {
			return null;
		} else {
			//returns the select String
			return node.valueOf("@statementAttribute");	
		}
	}
}
