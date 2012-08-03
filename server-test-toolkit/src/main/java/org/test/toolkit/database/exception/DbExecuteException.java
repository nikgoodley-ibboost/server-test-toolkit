package org.test.toolkit.database.exception;

public class DbExecuteException extends DbException {

	private static final long serialVersionUID = 1L;

	public DbExecuteException() {
	}

	public DbExecuteException(String message) {
		super(message);
	}

	public DbExecuteException(Throwable cause) {
		super(cause);
	}

	public DbExecuteException(String message, Throwable cause) {
		super(message, cause);
	}

	public DbExecuteException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
