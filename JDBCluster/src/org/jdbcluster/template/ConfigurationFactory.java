package org.jdbcluster.template;

import org.jdbcluster.exception.ConfigurationException;

/**
 * 
 * @author Christopher Schmidt
 * @author Philipp Noggler
 * implements ConfigurationFactory to use HIBERNATE
 */
public abstract class ConfigurationFactory {

	private static ConfigurationTemplate config = null;
	
	public static void setConfigurationClass(String TClass) {
		Class clazz;
		
		try {
			clazz = Class.forName(TClass, false, Thread.currentThread().getContextClassLoader());
			config = (ConfigurationTemplate) clazz.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ConfigurationException("no definition for the class [" + TClass + "] with the specified name could be found", e);
		} catch (InstantiationException e) {
			throw new ConfigurationException("specified class [" + TClass + "] object cannot be instantiated because it is an interface or is an abstract class", e);
		} catch (IllegalAccessException e) {
			throw new ConfigurationException("the currently executed ctor for class [" + TClass + "] does not have access", e);
		}
		
	}
	public static ConfigurationTemplate getInstance() {
		if (config == null) {
			throw new ConfigurationException("Configuration Class has not been set!", new Throwable());
		}
		return config;
	}
}
