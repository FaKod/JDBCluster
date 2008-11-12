package org.jdbcluster.exception;

/**
 * thrown if there is a privilege violation
 * @author FaKod
 *
 */
public class PrivilegeException extends JDBClusterException {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -1018800983736771225L;

	public PrivilegeException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public PrivilegeException(String message) {
		super(message);
	}
}
