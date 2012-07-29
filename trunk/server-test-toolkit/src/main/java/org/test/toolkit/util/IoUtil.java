package org.test.toolkit.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public final class IoUtil {

	private IoUtil() {
 	}

	/**
	 * @param inputStream
	 * @param localFilePath
	 * @throws IOException
	 */
	public static void InputStreamToFile(InputStream inputStream, String localFilePath)
			throws IOException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
		try {
			IOUtils.copy(inputStream, bufferedOutputStream);
		} finally{
			IOUtils.closeQuietly(bufferedOutputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}

}
