package org.test.toolkit.file;

import org.test.toolkit.util.ValidationUtil;

public class RandomFileFactory {

	public static RandomFile newRandomFile(FileType fileType) {
		ValidationUtil.checkNull(fileType);

		if (fileType == FileType.Text.TXT)
			return new TxtFile();
		if (fileType == FileType.Document.DOC)
			return new DocFile();
		if (fileType == FileType.Image.JPG)
			return new JpgFile();

		throw new UnsupportedOperationException("not support this file type: " + fileType.toString());
	}

	public static RandomFile newRandomFileForTxt() {
		return newRandomFile(FileType.Text.TXT);
	}

	public static RandomFile newRandomFileForJpg(int width, int height) {
		return new JpgFile(height, height);
	}

	public static RandomFile newRandomFileForTxt(long sizeByByteUnit) {
		return new TxtFile(sizeByByteUnit);
	}

	private RandomFileFactory(){
 	}
 }
