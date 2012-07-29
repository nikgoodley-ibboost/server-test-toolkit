package org.test.toolkit.server.ftp.command;

import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.util.JSchUtil.JSchChannelUtil;
import org.test.toolkit.util.ValidationUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public abstract class SftpCommand {

	private final static Logger LOGGER = Logger.getLogger(SftpCommand.class);

	private final Session session;

 	public SftpCommand(Session session) {
		ValidationUtil.nonNull(session);
		this.session = session;
	}

	public Session getSession() {
		return session;
	}

	public void executeWithoutResult(){
		_executeWithResult();
 	}

	protected Object _executeWithResult() {
		ChannelSftp channelSftp = null;
		try {
			channelSftp = openChannel();
			return runCommandByChannelWithResult(channelSftp);
		} catch (Exception e1) {
			LOGGER.error(e1.getMessage(), e1);
			throw new CommandExecuteException(e1.getMessage(), e1);
		} finally {
			JSchChannelUtil.disconnect(channelSftp);
		}
	}

	private ChannelSftp openChannel() throws JSchException {
		ChannelSftp channelSftp = JSchChannelUtil.getSftpChannel(session);
		channelSftp.connect();

		return channelSftp;
	}

	abstract Object runCommandByChannelWithResult(ChannelSftp channelSftp) throws SftpException;

}
