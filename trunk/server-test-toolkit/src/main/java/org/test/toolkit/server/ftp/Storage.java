package org.test.toolkit.server.ftp;

import java.io.FileNotFoundException;
import java.io.InputStream;

/**
 * @author fu.jian
 *
 */
public interface Storage {

	InputStream getFile(String storagePath) ;

	public void getFile(String storagePath, String localFilePath);

	public void storeFile(InputStream srcInputStream, String dstFolder, String dstFileName);

	public void storeFile(String localFilePath, String dstFolder, String dstFileName) throws FileNotFoundException;

}
