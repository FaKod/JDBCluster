package org.jdbcluster.exception;

public class ConfigurationException extends JDBClusterException {

	private static final long serialVersionUID = 1L;

	public ConfigurationException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public ConfigurationException(String message) {
		super(message);
	}

}
