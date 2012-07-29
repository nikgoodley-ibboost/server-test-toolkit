package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.test.toolkit.server.common.user.FtpUser;

public class FtpRemoteStorageImpl extends AbstractRemoteStroage {

	private FTPClient ftpClient;

	public FtpRemoteStorageImpl(FtpUser ftpUser) {
		super(ftpUser);
	}

	@Override
	public void connect() {
		if (ftpClient == null) {
			ftpClient = new FTPClient();
			try {
				ftpClient.connect(serverUser.getHost(), serverUser.getPort());
				ftpClient.login(serverUser.getUsername(), serverUser.getPassword());
 			} catch (SocketException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public InputStream getFile(String storagePath) throws IOException {
		return ftpClient.retrieveFileStream(storagePath);
	}

	@Override
	public void disconnect() {
		try {
			if (ftpClient != null)
				ftpClient.disconnect();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void putFile(InputStream srcInputStream, String dstFolder, String dstFileName)
			 {
		try {
			if (dstFolder != null) {
				ftpClient.changeWorkingDirectory(dstFolder);
			}
			ftpClient.storeFile(dstFileName, srcInputStream);
		} catch (IOException e) {
 		} finally {
			IOUtils.closeQuietly(srcInputStream);
		}
	}
}
