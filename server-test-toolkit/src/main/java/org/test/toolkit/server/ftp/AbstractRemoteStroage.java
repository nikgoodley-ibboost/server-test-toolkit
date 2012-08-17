package org.test.toolkit.server.ftp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.user.ServerUser;

public abstract class AbstractRemoteStroage implements RemoteStorage {

	private final static Logger LOGGER = Logger.getLogger(AbstractRemoteStroage.class);

	protected ServerUser serverUser;

	public AbstractRemoteStroage(ServerUser serverUser) {
		this.serverUser = serverUser;
		connect();
	}

	public ServerUser getServerUser() {
		return serverUser;
	}

	@Override
	public void download(String storagePath, String localFilePath) {
		LOGGER.info(String.format("[sftp]download file from %s to %s", storagePath, localFilePath));
		FileOutputStream outputStream = null;
		try {
			outputStream = new FileOutputStream(localFilePath);
			download(storagePath, outputStream);
		} catch (FileNotFoundException e) {
			throw new CommandExecuteException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(outputStream);
		}

	}


	@Override
	public void upload(String localFilePath, String dstFolder, String dstFileName) {
		LOGGER.info(String.format("[sftp]upload file from %s to %s", localFilePath, dstFolder));
		BufferedInputStream bufferedInputStream = null;
		try {
			bufferedInputStream = new BufferedInputStream(new FileInputStream(localFilePath));
			upload(bufferedInputStream, dstFolder, dstFileName);
		} catch (FileNotFoundException e) {
			throw new CommandExecuteException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(bufferedInputStream);
		}
	}
	

	@Override
	public String toString() {
		return "AbstractRemoteStroage [serverUser=" + serverUser + "]";
	}

}
