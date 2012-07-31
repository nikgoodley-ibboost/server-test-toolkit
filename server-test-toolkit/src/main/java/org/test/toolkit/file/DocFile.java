package org.test.toolkit.file;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.apache.poi.poifs.filesystem.DirectoryEntry;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

public class DocFile extends RandomFile {

	public static final String EXTENSION = FileType.Office.DOC.toString();
	private static final String WORD_TYPE = "WordDocument";

	public DocFile() {
		super(EXTENSION, getContentBytes());
	}

	private static byte[] getContentBytes() {
		byte byteArray[] = FileUtil.getRandomStringContent().getBytes();
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

		try {
			POIFSFileSystem fs = new POIFSFileSystem();
			DirectoryEntry directory = fs.getRoot();
			directory.createDocument(WORD_TYPE, byteArrayInputStream);
			fs.writeFilesystem(byteArrayOutputStream);
			return byteArrayOutputStream.toByteArray();
		} catch (IOException e) {
			e.printStackTrace();
			throw new RandomFileException("create doc file content fail", e);
		} finally {
			FileUtil.closeStream(byteArrayInputStream);
			FileUtil.closeStream(byteArrayOutputStream);
		}
	}
}
