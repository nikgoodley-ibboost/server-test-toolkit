package org.test.toolkit.server.ftp;

import org.test.toolkit.server.common.user.ServerUser;


/**
 * DEFAULT_PORT = {@code FtpUser#DEFAULT_PORT}
 * @author Administrator
 *
 */
public class FtpUser extends ServerUser{

	public final static int DEFAULT_PORT = 21;

 	public FtpUser(String host, int port, String username, String password) {
		super(host, port, username, password);
 	}

 	public FtpUser(String host, String username, String password) {
		super(host, DEFAULT_PORT, username, password);
 	}

}
