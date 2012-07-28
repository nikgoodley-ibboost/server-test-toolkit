package org.test.toolkit.server.user;

public class SshUser extends ServerUser {

	public static final int DEFAULT_PORT = 22;

	public SshUser(String host, String username, String password) {
		super(host, DEFAULT_PORT, username, password);
	}

	public SshUser(String host, int port, String username, String password) {
		super(host, port, username, password);
	}

}
