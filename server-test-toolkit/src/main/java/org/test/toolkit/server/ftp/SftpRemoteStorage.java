package org.test.toolkit.server.ftp;

import java.io.InputStream;
import java.io.OutputStream;

import org.apache.log4j.Logger;
import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;
import org.test.toolkit.server.ftp.command.sftp.SftpCommandWithoutResult;
import org.test.toolkit.server.ftp.command.sftp.SftpGetCommand;
import org.test.toolkit.server.ftp.command.sftp.SftpPutCommand;

import com.jcraft.jsch.Session;

public class SftpRemoteStorage extends AbstractRemoteStroage {

	private final static Logger LOGGER = Logger.getLogger(SftpRemoteStorage.class);

	private Session session;

	/**
	 * When get instance, the connection will be created by default, but you should call
	 * {@link #disconnect()} to release the connection.
	 * @param sshUser
	 */
	public SftpRemoteStorage(SshUser sshUser) {
		super(sshUser);
	}

	@Override
	public void disconnect() {
		JSchSessionUtil.disconnect(session);
	}

	@Override
	public void connect() {
		session = JSchSessionUtil.getSession(serverUser);
	}

	@Override
	public void download(String remotePath, OutputStream outputStream) {
		LOGGER.info(String.format("[storage]download  %s", remotePath));

		SftpCommandWithoutResult sftpGetCommand = new SftpGetCommand(session, remotePath, outputStream);
		sftpGetCommand.execute();
	}

	@Override
	public void upload(InputStream srcInputStream, String remoteFolder, String remoteFileName) {
		LOGGER.info(String.format("[storage]update file to  %s as %s", remoteFolder, remoteFileName));
		SftpCommandWithoutResult sftpPutCommand = new SftpPutCommand(session, srcInputStream, remoteFolder,
				remoteFileName);
		sftpPutCommand.execute();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
