package org.test.toolkit.file;

import java.util.Random;

public class TxtFile extends RandomFile {

	public static final String EXTENSION = ".txt";

	public TxtFile() {
		this(new Random().nextInt(10)+1);
	}

	public TxtFile(float sizeInKBUnit) {
		super(EXTENSION, getContentBytes(sizeInKBUnit));
	}

 	private static byte[] getContentBytes(float sizeInKBUnit) {
 		return FileUtil.generateBytesBySize(sizeInKBUnit);
	}

}
