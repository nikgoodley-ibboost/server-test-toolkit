package org.test.toolkit.server.ftp;

import java.io.InputStream;
import java.io.OutputStream;

import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;
import org.test.toolkit.server.ftp.command.sftp.SftpCommandWithoutResult;
import org.test.toolkit.server.ftp.command.sftp.SftpGetCommand;
import org.test.toolkit.server.ftp.command.sftp.SftpPutCommand;

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
	public void download(String storagePath,OutputStream outputStream) {
		SftpCommandWithoutResult sftpGetCommand = new SftpGetCommand(session, storagePath, outputStream);
		sftpGetCommand.execute();
 	}

	@Override
	public void upload(InputStream srcInputStream, String dstFolder, String dstFileName) {
		SftpCommandWithoutResult sftpPutCommand = new SftpPutCommand(session, srcInputStream, dstFolder,
				dstFileName);
		sftpPutCommand.execute();
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
