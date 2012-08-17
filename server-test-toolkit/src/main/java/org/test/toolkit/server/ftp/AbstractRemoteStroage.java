package org.test.toolkit.server.ftp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.apache.commons.io.IOUtils;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.user.ServerUser;

public abstract class AbstractRemoteStroage implements RemoteStorage {
  
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
