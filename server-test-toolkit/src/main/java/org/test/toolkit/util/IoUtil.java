package org.test.toolkit.util;

import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;

public final class IoUtil {

	private IoUtil() {
 	}

	public static void InputStreamToFile(InputStream inputStream, String localFilePath)
			throws FileNotFoundException, IOException {
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(localFilePath));
		try {
			IOUtils.copy(inputStream, bufferedOutputStream);
		} finally{
			IOUtils.closeQuietly(bufferedOutputStream);
			IOUtils.closeQuietly(inputStream);
		}
	}

}
