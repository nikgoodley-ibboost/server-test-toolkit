package org.test.toolkit.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public final class IoUtil {

 	/**
	 * @param inputStream
	 * @param filePath
	 * @throws IOException
	 */
	public static void inputStreamToFile(InputStream inputStream, String filePath)
			throws IOException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
		try {
			IOUtils.copy(inputStream, bufferedOutputStream);
		} finally{
			IOUtils.closeQuietly(bufferedOutputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}

	private IoUtil() {
 	}

}
