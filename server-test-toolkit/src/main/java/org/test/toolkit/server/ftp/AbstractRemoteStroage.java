package org.test.toolkit.server.ftp;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.test.toolkit.server.user.ServerUser;
import org.test.toolkit.util.IoUtil;

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
	public void getFile(String storagePath, String localFilePath) throws IOException {
		InputStream inputStream = getFile(storagePath);
		IoUtil.InputStreamToFile(inputStream, localFilePath);
	}

	@Override
	public String toString() {
		return "AbstractRemoteStroage [serverUser=" + serverUser + "]";
	}

	@Override
	public void putFile(String localFilePath, String dstFolder, String dstFileName) throws IOException {
		BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(
				localFilePath));
		try {
			putFile(bufferedInputStream, dstFolder,dstFileName);
		} finally {
			IOUtils.closeQuietly(bufferedInputStream);
		}
	}

}
