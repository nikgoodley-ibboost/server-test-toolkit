package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.test.toolkit.server.common.user.SshUser;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpRemoteStorageImpl extends AbstractRemoteStroage {

	private Session session;

	public SftpRemoteStorageImpl(SshUser sshUser) {
		super(sshUser);

	}

	@Override
	public void close() {
		if (session != null)
			session.disconnect();
	}

	@Override
	public void connect() {
		JSch jsch = new JSch();
		try {
			session = jsch.getSession(serverUser.getHost(), serverUser.getPassword(),
					serverUser.getPort());
			if (serverUser.getPassword() != null) {
				session.setPassword(serverUser.getPassword());
			}

			Properties config = new Properties();
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();
		} catch (JSchException e) {
			e.printStackTrace();
		}

	}

	@Override
	public InputStream getFile(String storagePath) throws IOException {
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			return channel.get(storagePath);

		} catch (JSchException e1) {
			e1.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
				channel.quit();
			}
		}
		return null;
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void putFile(InputStream srcInputStream, String dstFolder, String dstFileName) {
		ChannelSftp channel = null;
		try {
			channel = (ChannelSftp) session.openChannel("sftp");
			channel.connect();
			
			if(dstFolder!=null)
				channel.cd(dstFolder);
			channel.put(srcInputStream, dstFileName);

		} catch (JSchException e1) {
			e1.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			if (channel != null) {
				channel.disconnect();
				channel.quit();
			}
		}
 	}

}
