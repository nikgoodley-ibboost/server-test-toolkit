package org.test.toolkit.file;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;

public final class FileUtil {

	private FileUtil(){

	}

	public static String calcuteSHA(byte[] byteArray) {
		MessageDigest messageDigest = null;

		try {
			messageDigest = MessageDigest.getInstance("SHA1");
			messageDigest.update(byteArray);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return new String(FileUtil.byteTohex(messageDigest.digest()));
	}

	/**
	 * Design for big file
	 *
	 * @param inputStream
	 * @return
	 */
	public static String calcuteSHA(InputStream inputStream) {
		BufferedInputStream bufferedInputStream=new BufferedInputStream(inputStream);
		MessageDigest messageDigest = null;
		// allocate 1M
		byte[] b = new byte[1024 * 1024 * 1];
		int length = 0;
		try {
			messageDigest = MessageDigest.getInstance("SHA1");
			while ((length = bufferedInputStream.read(b)) != -1) {
				messageDigest.update(b, 0, length);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}finally{
			if(bufferedInputStream!=null)
				try {
					bufferedInputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}

		return new String(FileUtil.byteTohex(messageDigest.digest()));
	}

	private static String byteTohex(byte[] bts) {
		String des = "";
		String tmp = null;

		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	static byte[] generateBytesBySize(float sizeByKBUnit) {
		int targetFileSize = (int) (sizeByKBUnit * (1<<10));
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

				int needBytesCount = targetFileSize - storeSize;
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

	static String newContent() {
		return UUID.randomUUID().toString();
	}

	public static void closeStream(Closeable closeable) {
		try {
			if (closeable != null)
				closeable.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
