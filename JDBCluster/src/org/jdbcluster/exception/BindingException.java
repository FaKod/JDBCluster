package org.jdbcluster.exception;

/**
 * 
 * @author Philipp Noggler
 * BindingException is a Runtime Exception
 *
 */
public class BindingException extends JDBClusterException {

	
	private static final long serialVersionUID = 1L;

	public BindingException(Throwable cause) {
		super(cause);
	}
	
	public BindingException(String message, Throwable cause) {
		super(message, cause);
	}
}
