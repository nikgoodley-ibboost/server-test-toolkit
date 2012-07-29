package org.test.toolkit.server.ftp.command;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class SftpCommandWithoutResult extends SftpCommand {

	public SftpCommandWithoutResult(Session session) {
		super(session);
	}

	@Override
	Object runCommandByChannelWithResult(ChannelSftp channelSftp) throws SftpException {
		runCommandByChannelWithoutResult(channelSftp);
		return null;
	}

	protected abstract void runCommandByChannelWithoutResult(ChannelSftp channelSftp) throws SftpException;
}
