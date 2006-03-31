package org.jdbcluster.exception;

/**
 * thrown if there is a privilege violation
 * @author FaKod
 *
 */
public class PrivilegeException extends JDBClusterException {
	
	public PrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PrivilegeException(String message) {
		super(message);
	}
}
