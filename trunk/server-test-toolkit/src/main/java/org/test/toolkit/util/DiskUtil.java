package org.test.toolkit.util;

import java.io.File;

public class DiskUtil {

	private final File file;

	private DiskUtil(String filePath) {
		super();
		ValidationUtil.isExistedFile(filePath);
		this.file = new File(filePath);
	}

	public static DiskUtil getInstance(String filePath) {
		return new DiskUtil(filePath);
	}

	public long getTotalSpace() {
		return file.getTotalSpace();
	}

	/**
	 * * according to disk file path right and other control factor to computer.
	 * * @return
	 */
	public long getUsableSpace() {
		return file.getUsableSpace();
	}

	public File getFile() {
		return file;
	}

	public String getAbsolutePath() {
		return file.getAbsolutePath();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DiskUtil [file=");
		builder.append(file.getAbsolutePath());
		builder.append("]");
		return builder.toString();
	}
}