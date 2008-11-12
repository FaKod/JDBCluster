package org.jdbcluster;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class JDBClusterConfigurationBase extends SAXReader implements
		JDBClusterConfiguration {

	protected Document document;

	// path to config file
	protected String configuration;

	public void addExtension(String extensionPath) {
		try {
			Document doc = read(extensionPath);
			Element extensionRoot = doc.getRootElement();
			document.getRootElement().add(extensionRoot.detach());
		} catch (DocumentException e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
}
