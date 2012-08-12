package org.test.toolkit.server.ftp;

import java.io.InputStream;

/**
 * @author fu.jian
 *
 */
public interface Storage {

	public InputStream getFile(String storagePath);

	public void storeFile(InputStream srcInputStream, String dstFolder, String dstFileName);

}
