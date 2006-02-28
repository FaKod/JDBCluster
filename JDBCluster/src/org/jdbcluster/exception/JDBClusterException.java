package org.jdbcluster.exception;

/**
 * 
 * @author Christopher Schmidt
 * JDBClusterException is an abstract Runtime Exception
 *
 */

public abstract class JDBClusterException extends RuntimeException {
	
	public JDBClusterException(Throwable cause) {
		super(cause);
	}
	
	public JDBClusterException(String message) {
		super(message);
	}
	
	public JDBClusterException(String message, Throwable cause) {
		super(message, cause);
	}

}
