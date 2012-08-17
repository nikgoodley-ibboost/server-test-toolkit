package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.exception.ServerConnectionException;
import org.test.toolkit.util.IoUtil;

public class FtpRemoteStorage extends AbstractRemoteStroage {

	private final static Logger LOGGER = Logger.getLogger(FtpRemoteStorage.class);

	private FTPClient ftpClient;

	public FtpRemoteStorage(FtpUser ftpUser) {
		super(ftpUser);
	}

	@Override
	public void connect() {
		if (ftpClient == null) {
			ftpClient = new FTPClient();
			try {
				ftpClient.connect(serverUser.getHost(), serverUser.getPort());
				ftpClient.login(serverUser.getUsername(), serverUser.getPassword());
			} catch (Exception e) {
				LOGGER.error(e.getMessage(), e);
				throw new ServerConnectionException(e.getMessage(), e);
			}
		}
	}

	@Override
	public void disconnect() {
		try {
			if (ftpClient != null)
				ftpClient.disconnect();
		} catch (IOException e) {
			LOGGER.warn("release connection fail for: " + e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return super.toString();
	}

	@Override
	public void upload(InputStream srcInputStream, String dstFolder, String dstFileName) {
		try {
			if (dstFolder != null) {
				ftpClient.changeWorkingDirectory(dstFolder);
			}
			ftpClient.storeFile(dstFileName, srcInputStream);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
			throw new CommandExecuteException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(srcInputStream);
		}
	}

	@Override
	public void download(String remotePath, OutputStream outputStream) {
		try {
			 InputStream inputStream = ftpClient.retrieveFileStream(remotePath);
			 IoUtil.inputStreamToOutputStream(inputStream, outputStream);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
			throw new ServerConnectionException(e.getMessage(), e);
		}		
	}
}
