package org.jdbcluster.exception;

public class DBException extends JDBClusterException {

	public DBException(Throwable cause) {
		super(cause);
	}
	
	public DBException(String message, Throwable cause) {
		super(message, cause);
	}
	
	public DBException(String message) {
		super(message);
	}

}
