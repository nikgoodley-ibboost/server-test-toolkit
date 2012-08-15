package org.test.toolkit.services.exception;

public class ServiceConnectionException extends ServiceException {

	private static final long serialVersionUID = 1L;

	public ServiceConnectionException() {
 	}

	public ServiceConnectionException(String arg0) {
		super(arg0);
 	}

	public ServiceConnectionException(Throwable arg0) {
		super(arg0);
 	}

	public ServiceConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
 	}

	public ServiceConnectionException(String arg0, Throwable arg1,
			boolean arg2, boolean arg3) {
		super(arg0, arg1, arg2, arg3);
 	}

}
