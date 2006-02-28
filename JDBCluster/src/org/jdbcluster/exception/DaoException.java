package org.jdbcluster.exception;

public class DaoException extends JDBClusterException {
	
	public DaoException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DaoException(String message) {
		super(message);
	}
}
