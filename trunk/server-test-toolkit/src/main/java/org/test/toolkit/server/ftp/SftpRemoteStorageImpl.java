package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;
import org.test.toolkit.server.ftp.command.GetSftpCommand;
import org.test.toolkit.server.ftp.command.PutSftpCommand;

import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpRemoteStorageImpl extends AbstractRemoteStroage {

	private Session session;

	/**
	 * @param sshUser
	 */
	public SftpRemoteStorageImpl(SshUser sshUser) {
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
	public InputStream getFile(String storagePath) throws IOException {
		GetSftpCommand sftpGetCommand = new GetSftpCommand(session, storagePath);
		try {
			return (InputStream) sftpGetCommand.execute();
		} catch (SftpException e) {
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public void putFile(InputStream srcInputStream, String dstFolder, String dstFileName) {
		PutSftpCommand sftpPutCommand = new PutSftpCommand(session, srcInputStream, dstFolder,
				dstFileName);
		try {
			sftpPutCommand.execute();
		} catch (SftpException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
