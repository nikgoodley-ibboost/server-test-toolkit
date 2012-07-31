package org.test.toolkit.file;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public abstract class RandomFile {

 	protected String fileName;
	protected String sha;
	protected String fileContent;
	protected String extension;
	protected long fileSize;
 	protected InputStream fileInputStream;
	protected byte[] byteArray;


	public long getFileSize() {
		return fileSize;
	}

	public String getFileName() {
		return fileName;
	}

	public String getSha() {
		return sha;
	}

	/**
	 * @return it is better not to use. instead by getFileInputStream();
	 */
	@Deprecated
	public String getFileContent() {
		return fileContent;
	}

	public String getExtension() {
		return extension;
	}


 	public InputStream getFileInputStream() {
		return new ByteArrayInputStream(byteArray);
	}

	public RandomFile(String extension,byte[] byteArray) {
		this.extension=extension;
		this.byteArray=byteArray;
		fileSize=byteArray.length;
		sha = FileUtil.calcuteSHA(byteArray);
		fileContent = byteArray.toString();
		fileName = formatFileName();

		System.out.println(toString());
	}

	@Override
	public String toString() {
		StringBuilder stringBuilder = new StringBuilder("================ ["
				+ this.extension.toUpperCase().substring(1) + "] ================");
		String format = "\n|| =>[%-9s]:[%s]";

		stringBuilder.append(String.format(format, "filename", this.fileName));
		stringBuilder.append(String.format(format, "filesize", this.fileSize+"B"));
		stringBuilder.append(String.format(format, "sha", this.sha));
 		stringBuilder
				.append(String.format(format, "extension", this.extension));
		stringBuilder.append("\n=======================================");

		return stringBuilder.toString();
	}


	private String formatFileName() {
		return new SimpleDateFormat("yyyyMMddhhmm").format(new Date())
				.substring(4)
				+ "_" + sha + extension;
	}

	public static String newContent(){
		return UUID.randomUUID().toString();
	}

	public enum FileType {
		TXT, JPG, DOC;

		public String toString() {
			return "." + super.toString().toLowerCase();
		};

	}

}
