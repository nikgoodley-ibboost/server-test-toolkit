package org.test.toolkit.server.ftp;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author fu.jian
 * 
 */
public interface Storage {

	InputStream getFile(String storagePath) throws IOException;

	public void getFile(String storagePath, String localFilePath) throws IOException;

	public void putFile(InputStream srcInputStream, String dstFolder, String dstFileName);

	public void putFile(String localFilePath, String dstFolder, String dstFileName) throws IOException;

}
