package org.test.toolkit.server.ftp.command.sftp;

import com.jcraft.jsch.Session;

public abstract class SftpCommandWithResult extends SftpCommand {

	public SftpCommandWithResult(Session session) {
		super(session);
	}

	public Object executeWithResult() {
		return super._execute();
	}

}
