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
package org.jdbcluster.clustertype;

import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.Node;
import org.jdbcluster.JDBClusterConfigurationBase;

/**
 * class ClusterTypeConfig is an implementation of ClusterTypeConfig.
 * It saves the location of the configuration file and
 * exexutes some queries on that XML file.
 * @author Philipp Noggler
 * @author Tobi
 */

public class ClusterTypeConfigImpl extends JDBClusterConfigurationBase implements ClusterTypeConfig {
		
	/**
	 * Default constructor
	 *
	 */
	public ClusterTypeConfigImpl() {}
	
	/**
	 * Constructor which sets the config path
	 * @param config the configuration to be set
	 */
	public ClusterTypeConfigImpl(String config) {
		this.setConfiguration(config);
	}
	
	/**
	 * returns the config path
	 * @return String
	 */
	public String getConfiguration() {
		return configuration;
	}
	
	/**
	 * sets the path, reads out the document and saves it
	 * @param configuration the config path
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
	 * returns the name of the specified clustertype 
	 * or null if there is none (via xPath)
	 * @param CTName ClusterType name
	 * @return String
	 */
	public String getClusterType(String CTName) {
		
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + CTName + "']";
		Node node = document.selectSingleNode(xPath);

		if (node == null) {
			return null;
		} else {
			return node.valueOf("@id");
		}
	}
	
	/**
	 * returns the classname of the given ClusterType
	 * @deprecated use {@link ClusterType.getClusterClass()} instead
	 * @param ct ClusterType
	 * @return String
	 */
	@Deprecated
	public String getClusterClassName(String clusterTypeName) {
		//xPath expression to get the classname
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + clusterTypeName + "']";

		Node node = document.selectSingleNode(xPath);
		
		if (node == null) {
			return null;
		} else {
			//returns the classname as a String
			return node.valueOf("@class");	
		}
	}
	
	/**
	 * returns the classname of the given ClusterType
	 * @param CTName the ClusterType's name as a String
	 * @return String
	 */
	public String getClassName(String CTName) {

		//xPath expression to get the classname
		String xPath = "//jdbcluster/clustertype/cluster[@id='" + CTName + "']";

		Node node = document.selectSingleNode(xPath);
		
		if (node == null) {
			return null;
		} else {
			//returns the classname as a String
			return node.valueOf("@class");	
		}
	}
	
	/**
	 * returns a list of all cluster ids
	 * @return List<String>
	 */
	@SuppressWarnings("unchecked")
	public List<String> getClusterIDs() {
		ArrayList<String> clusterIDs = new ArrayList<String>();
		//xPath expression to get the classname
		String xPath = "//jdbcluster/clustertype/cluster";

		List<Node> nodes = document.selectNodes(xPath);
		if(nodes != null) {
			for(Node n : nodes){
				clusterIDs.add(n.valueOf("@id"));
			}
		}
		return clusterIDs;
	}

	/**
	 * class name of cluster interceptor
	 * @return String
	 */
	public String getClusterInterceptorClassName() {
		String xPath = "//jdbcluster/clustertype";

		Node node = document.selectSingleNode(xPath);
		
		if (node == null) {
			return null;
		} else {
			//returns the classname as a String
			return node.valueOf("@clusterInterceptor");	
		}
	}
}
