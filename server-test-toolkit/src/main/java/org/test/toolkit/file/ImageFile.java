package org.test.toolkit.file;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Random;

import com.sun.image.codec.jpeg.ImageFormatException;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

@SuppressWarnings("restriction")
public class ImageFile extends RandomFile {

	public static final String EXTENSION = ".jpg";

	public ImageFile(int width, int height) {
		super(EXTENSION, getContentBytes(width, height));
	}

	public ImageFile() {
		this(getRandomWidthOrHeight(), getRandomWidthOrHeight());

	}

	private static int getRandomWidthOrHeight() {
		return new Random().nextInt(1024);
	}

	private static byte[] getContentBytes(int width, int height) {
		if (width <= 0 || height <= 0)
			throw new IllegalArgumentException(String.format("width[%d] or height[%d] < 0 "
					+ System.getProperty("line.seperator"), width, height));

		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
		Graphics createGraphics = bufferedImage.getGraphics();
		createGraphics.drawString(FileUtil.newContent(), 0, height / 2);

		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(byteArrayOutputStream);
		try {
			JPEGImageEncoder createJPEGEncoder = JPEGCodec.createJPEGEncoder(bufferedOutputStream);
			createJPEGEncoder.encode(bufferedImage);
			return byteArrayOutputStream.toByteArray();
		} catch (ImageFormatException e1) {
			e1.printStackTrace();
			throw new RandomFileException(
					"image format exception when generating image file content", e1);
		} catch (IOException e2) {
			e2.printStackTrace();
			throw new RandomFileException("io exception when generating image file content", e2);
		} finally {
			FileUtil.closeStream(bufferedOutputStream);
		}

	}

}
