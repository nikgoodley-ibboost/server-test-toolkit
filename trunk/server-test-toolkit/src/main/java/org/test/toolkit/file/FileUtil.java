package org.test.toolkit.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.test.toolkit.util.DigestUtil;
import org.test.toolkit.util.DigestUtil.DigestAlgorithm;

public final class FileUtil {

	private FileUtil(){

	}

	static String calcuteSHA_1(byte[] byteArray) {
 		return DigestUtil.getDigest(byteArray, DigestAlgorithm.SHA_1);
 	}

	static String calcuteSHA_1(InputStream inputStream) throws IOException {
 		return DigestUtil.getDigest(inputStream, DigestAlgorithm.SHA_1);

	}

 	static byte[] generateBytesBySize(long sizeByByteUnit) {
		long targetFileSize = sizeByByteUnit;
		int allocateSize = 32;

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(
				byteOutputStream, 1024 * 512);

		int storeSize = 0;
		byte[] bytes = null;
		try {
			while (storeSize < targetFileSize) {
				byte[] array = new byte[allocateSize];
				Arrays.fill(array, (byte) (new Random().nextInt()));

				int needBytesCount = (int) (targetFileSize - storeSize);
				if (allocateSize > needBytesCount)
					bufferedOutputStream.write(array, 0, needBytesCount);
				else
					bufferedOutputStream.write(array);
				storeSize += allocateSize;
			}
			bufferedOutputStream.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			byte[] byteArray = byteOutputStream.toByteArray();
			bytes = byteArray;
			if (bufferedOutputStream != null)
				try {
					bufferedOutputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

		}

		return bytes;
	}

	static String newRandomStringContent() {
		return UUID.randomUUID().toString();
	}
 
}
