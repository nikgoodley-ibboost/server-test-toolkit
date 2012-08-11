package org.test.toolkit.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

import org.apache.commons.io.IOUtils;
import org.test.toolkit.util.DigestUtil;
import org.test.toolkit.util.DigestUtil.DigestAlgorithm;
import org.test.toolkit.util.ValidationUtil;

public final class FileUtil {

	private FileUtil() {
 	}

	static String calcuteSHA_1(byte[] byteArray) {
		return DigestUtil.getDigest(byteArray, DigestAlgorithm.SHA_1);
	}

	static String calcuteSHA_1(InputStream inputStream) throws IOException {
		return DigestUtil.getDigest(inputStream, DigestAlgorithm.SHA_1);
 	}

	static byte[] generateBytesBySize(long sizeByByteUnit) {
		ValidationUtil.checkPositive(sizeByByteUnit);

		int allocateSize = 32;

		ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteOutputStream, 1024 * 512);

		int storeSize = 0;
		byte[] bytes = null;
		try {
			while (storeSize < sizeByByteUnit) {
				byte[] array = new byte[allocateSize];
				Arrays.fill(array, (byte) (new Random().nextInt()));

				int needBytesCount = (int) (sizeByByteUnit - storeSize);
				if (allocateSize > needBytesCount)
					bufferedOutputStream.write(array, 0, needBytesCount);
				else
					bufferedOutputStream.write(array);
				storeSize += allocateSize;
			}
			bufferedOutputStream.flush();
		} catch (IOException e) {
			throw new RandomFileException(e.getMessage(),e);
		} finally {
			bytes = byteOutputStream.toByteArray();
			IOUtils.closeQuietly(bufferedOutputStream);
 		}

		return bytes;
	}

	static int getRandomSizeInByteUnit() {
		return new Random().nextInt(10000) + 1;
	}

	static byte[] getRandomBytes() {
		return getRandomString().getBytes();
	}

	static String getRandomString() {
		return UUID.randomUUID().toString();
	}

}
