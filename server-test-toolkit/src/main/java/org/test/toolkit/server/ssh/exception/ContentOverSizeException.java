package org.test.toolkit.server.ssh.exception;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class ContentOverSizeException extends UncheckedServerOperationException {

	private static final long serialVersionUID = 1L;

	public ContentOverSizeException() {
 	}

	public ContentOverSizeException(String message) {
		super(message);
 	}

	public ContentOverSizeException(Throwable cause) {
		super(cause);
 	}

	public ContentOverSizeException(String message, Throwable cause) {
		super(message, cause);
 	}

}
