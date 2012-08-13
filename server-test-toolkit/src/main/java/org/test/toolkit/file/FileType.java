package org.test.toolkit.file;

enum FileCategory {
	Image, DOCUMENT, PLAIN_TXT, OTHER;
}

public enum FileType {

	DOC(FileCategory.DOCUMENT), JPG(FileCategory.Image), GIF(FileCategory.Image), TXT(FileCategory.PLAIN_TXT), PDF(
			FileCategory.OTHER);

	private FileCategory type;

	private FileType(FileCategory t) {
		this.type = t;
	}

	public FileCategory getType() {
		return type;
	}

	public String toString() {
		return "." + super.toString().toLowerCase();
	}

	public String getExtension() {
		return toString();
	}
}