package org.test.toolkit.file;

/**
 * @author fu.jian
 * @date Jul 31, 2012
 */
public class TxtFile extends RandomFile {

	public static final String EXTENSION = FileType.PlainText.TXT.toString();

	public TxtFile() {
		this(FileUtil.getRandomBytes());
	}

	public TxtFile(long sizeInByteUnit) {
		super(EXTENSION, FileUtil.generateBytesBySize(sizeInByteUnit));
	}

}
