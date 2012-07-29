package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;

import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.server.common.util.JSchUtil.JSchChannelUtil;
import org.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SftpRemoteStorageImpl extends AbstractRemoteStroage {

	private Session session;

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
		ChannelSftp channelSftp = null;
		try {
			channelSftp = openChannel();
			return channelSftp.get(storagePath);
		} catch (JSchException e1) {
			e1.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			JSchChannelUtil.disconnect(channelSftp);
		}
		return null;
	}

	private ChannelSftp openChannel() throws JSchException {
		ChannelSftp channelSftp = JSchChannelUtil.getSftpChannel(session);
		channelSftp.connect();

		return channelSftp;
	}

	@Override
	public void putFile(InputStream srcInputStream, String dstFolder, String dstFileName) {
		ChannelSftp channelSftp = null;
		try {
			channelSftp = openChannel();

			if (dstFolder != null)
				channelSftp.cd(dstFolder);
			channelSftp.put(srcInputStream, dstFileName);

		} catch (JSchException e1) {
			e1.printStackTrace();
		} catch (SftpException e) {
			e.printStackTrace();
		} finally {
			JSchChannelUtil.disconnect(channelSftp);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

}
