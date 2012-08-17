package org.test.toolkit.server.ftp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.user.ServerUser;
import org.test.toolkit.util.IoUtil;

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
	public void getFile(String storagePath, String localFilePath) {
		LOGGER.info(String.format("[sftp]download file from %s to %s", storagePath, localFilePath));
		InputStream inputStream = getFile(storagePath);
		try {
			IoUtil.inputStreamToFile(inputStream, localFilePath);
		} catch (Exception e) {
			throw new CommandExecuteException(e.getMessage(), e);
		}
	}

	@Override
	public String toString() {
		return "AbstractRemoteStroage [serverUser=" + serverUser + "]";
	}

	@Override
	public void storeFile(String localFilePath, String dstFolder, String dstFileName)
			throws FileNotFoundException {
		LOGGER.info(String.format("[sftp]update file from %s to %s", localFilePath, dstFolder));
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(localFilePath));
		try {
			storeFile(bufferedInputStream, dstFolder, dstFileName);
		} finally {
			IOUtils.closeQuietly(bufferedInputStream);
		}
	}

}
