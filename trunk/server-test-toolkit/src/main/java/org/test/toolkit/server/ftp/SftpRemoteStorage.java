package org.test.toolkit.server.ftp;

import java.io.InputStream;

import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;
import org.test.toolkit.server.ftp.command.sftp.SftpGetCommand;
import org.test.toolkit.server.ftp.command.sftp.SftpPutCommand;
import org.test.toolkit.server.ftp.command.sftp.SftpCommandWithResult;
import org.test.toolkit.server.ftp.command.sftp.SftpCommandWithoutResult;

import com.jcraft.jsch.Session;

public class SftpRemoteStorage extends AbstractRemoteStroage {

	private Session session;

	/**
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
	public InputStream getFile(String storagePath) {
		SftpCommandWithResult sftpGetCommand = new SftpGetCommand(session, storagePath);
 		return (InputStream) sftpGetCommand.executeWithResult();
 	}

	@Override
	public void storeFile(InputStream srcInputStream, String dstFolder, String dstFileName) {
		SftpCommandWithoutResult sftpPutCommand = new SftpPutCommand(session, srcInputStream, dstFolder,
				dstFileName);
		sftpPutCommand.execute();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
