package org.test.toolkit.server.ftp;

import java.io.FileNotFoundException;

import org.test.toolkit.server.common.interfaces.RemoteConnection;

public interface RemoteStorage extends RemoteConnection, Storage{

	public void getFile(String storagePath, String localFilePath);

	public void storeFile(String localFilePath, String dstFolder, String dstFileName) throws FileNotFoundException;

}
