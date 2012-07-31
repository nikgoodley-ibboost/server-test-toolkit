package org.test.toolkit.file;

import org.test.toolkit.file.RandomFile.FileType;

public class RandomFileFactory {

	public static RandomFile newRandomFile(FileType extension) {
		switch (extension) {
		case TXT:
			return new TxtFile();
		case DOC:
			return new DocFile();
		case JPG:
			return new ImageFile();
		default:
			throw new AssertionError("no matched key");
		}
	}

	public static RandomFile newRandomFileForTXT() {
		return newRandomFile(FileType.TXT);
	}

	public static RandomFile newRandomFileForImage(int width, int height) {
		return new ImageFile(height, height);
	}

	public static RandomFile newRandomFileForTXT(float sizeByKbUnit) {
		return new TxtFile(sizeByKbUnit);
	}
}
