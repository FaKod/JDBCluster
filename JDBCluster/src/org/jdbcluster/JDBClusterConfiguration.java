package org.jdbcluster;

public interface JDBClusterConfiguration {

	/**
	 * This method extends the content of jdbcluster.conf.xml file with the content
	 * provided by the parameter.
	 * @param extensionPath the path to the extension file. The content of this file is merged with the 
	 * base configuration file. If new filter elements are defined for an existing cluster those elements
	 * are added. If the new filter elements are already specified in the jdbcluster.conf.xml they are overridden
	 * with the new values specified within the extension file.
	 */
	public void addExtension(String extensionPath);
}
