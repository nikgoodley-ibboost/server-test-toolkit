package org.test.toolkit.file;

/**
 * @author fu.jian
 * @date Jul 31, 2012
 */
public class TxtFile extends RandomFile {

	public static final String EXTENSION = FileType.Text.TXT.toString();

	public TxtFile() {
		this(FileUtil.getRandomSizeInByteUnit());
	}

	public TxtFile(long sizeInByteUnit) {
		super(EXTENSION, FileUtil.generateBytesBySize(sizeInByteUnit));
	}

}