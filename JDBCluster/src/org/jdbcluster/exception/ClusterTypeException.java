package org.jdbcluster.exception;

/**
 * 
 * @author Philipp Noggler
 * ClusterTypeException is a Runtime Exception
 *
 */

public class ClusterTypeException extends JDBClusterException {

	private static final long serialVersionUID = 5820380348594923264L;

	public ClusterTypeException(Throwable cause) {
		super(cause);
	}
	
	public ClusterTypeException(String message, Throwable cause) {
		super(message, cause);
	}


}
