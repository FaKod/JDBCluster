package org.jdbcluster.exception;

/**
 * 
 * @author Christopher Schmidt
 * CCFilterException is a Runtime Exception
 *
 */

public class CCFilterException extends JDBClusterException {

	
	private static final long serialVersionUID = -6741464722651366012L;

	public CCFilterException(Throwable cause) {
		super(cause);
	}
	
	public CCFilterException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public CCFilterException(String message) {
		super(message);
	}
}
