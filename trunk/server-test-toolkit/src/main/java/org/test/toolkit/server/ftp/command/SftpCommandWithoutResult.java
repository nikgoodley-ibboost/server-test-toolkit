package org.test.toolkit.server.ftp.command;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class SftpCommandWithoutResult extends SftpCommand {

	public SftpCommandWithoutResult(Session session) {
		super(session);
	}

	public void execute() {
		super._execute();
	}

	@Override
	Object runCommandByChannel(ChannelSftp channelSftp) throws SftpException {
		executeWithoutResult(channelSftp);
		return null;
	}

	protected abstract void executeWithoutResult(ChannelSftp channelSftp) throws SftpException;
}
