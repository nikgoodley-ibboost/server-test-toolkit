package org.test.toolkit.util;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.io.IOUtils;

public final class IoUtil {

	/**
	 * @param inputStream
	 * @param filePath
	 * @throws IOException
	 */
	public static void inputStreamToFile(InputStream inputStream, String filePath) throws IOException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(filePath));
		inputStreamToOutputStream(inputStream, bufferedOutputStream);
 	}

	public static void inputStreamToOutputStream(InputStream inputStream, OutputStream outputStream)
			throws IOException {
		try {
			IOUtils.copy(inputStream, outputStream);
		} finally {
			IOUtils.closeQuietly(outputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}

	private IoUtil() {
	}

}
