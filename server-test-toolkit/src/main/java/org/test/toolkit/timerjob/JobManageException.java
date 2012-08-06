package org.test.toolkit.timerjob;

public class JobManageException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public JobManageException() {
	}

	public JobManageException(String message) {
		super(message);
	}

	public JobManageException(Throwable cause) {
		super(cause);
	}

	public JobManageException(String message, Throwable cause) {
		super(message, cause);
	}

}
