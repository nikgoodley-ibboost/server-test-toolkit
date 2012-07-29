package org.test.toolkit.server.ftp.command;

import org.test.toolkit.util.ValidationUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class GetSftpCommand extends SftpCommandWithResult {

	private String storagePath;

	public GetSftpCommand(Session session, String storagePath) {
		super(session);
		ValidationUtil.nonNull(storagePath);

		this.storagePath = storagePath;
	}

	@Override
	public Object runCommandByChannel(ChannelSftp channelSftp) throws SftpException {
		return channelSftp.get(storagePath);
	}

}
