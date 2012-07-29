package org.test.toolkit.server.ftp.command;

import org.test.toolkit.server.common.util.JSchUtil.JSchChannelUtil;
import org.test.toolkit.util.ValidationUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class SftpCommand {

	private final Session session;

	public SftpCommand(Session session) {
		ValidationUtil.nonNull(session);
		this.session = session;
	}

	private ChannelSftp openChannel() throws JSchException {
		ChannelSftp channelSftp = JSchChannelUtil.getSftpChannel(session);
		channelSftp.connect();

		return channelSftp;
	}

	public Object execute() throws SftpException {
		ChannelSftp channelSftp = null;
		try {
			channelSftp = openChannel();
			return execute(channelSftp);
		} catch (JSchException e1) {
			e1.printStackTrace();
		} finally {
			JSchChannelUtil.disconnect(channelSftp);
		}
		return null;
	}

	abstract Object execute(ChannelSftp channelSftp) throws SftpException;

}
