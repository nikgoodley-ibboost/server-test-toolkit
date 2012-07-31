package org.test.toolkit.file;


public class RandomFileFactory {

	public static RandomFile newRandomFile(FileType extension) {
		switch (extension) {
		case TXT:
			return new TxtFile();
		case DOC:
			return new DocFile();
		case JPG:
			return new JpgFile();
		default:
			throw new AssertionError("no matched key");
		}
	}

	public static RandomFile newRandomFileForTxt() {
		return newRandomFile(FileType.PlainText.TXT);
	}

	public static RandomFile newRandomFileForJpg(int width, int height) {
		return new JpgFile(height, height);
	}

	public static RandomFile newRandomFileForTxt(float sizeByKbUnit) {
		return new TxtFile(sizeByKbUnit);
	}
}
