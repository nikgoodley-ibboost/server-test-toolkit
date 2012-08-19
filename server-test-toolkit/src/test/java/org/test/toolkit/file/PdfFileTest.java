package org.test.toolkit.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.junit.Assert;
import org.junit.Test;
import org.test.toolkit.util.IoUtil;

public class PdfFileTest {

	private static final String TARGET_FILE_PATH = "c:\\1.pdf";

	@Test
	public void test() {

		RandomFile newRandomFile = RandomFileFactory
				.newRandomFile(FileType.PDF);
		InputStream inputStream = newRandomFile.getInputStream();
		try {
			System.out.println(inputStream.available());
			IoUtil.inputStreamToFile(inputStream, TARGET_FILE_PATH);
		} catch (IOException e) {
			Assert.fail();
		}
		Assert.assertTrue(new File(TARGET_FILE_PATH).exists());
 		Assert.assertTrue(new File(TARGET_FILE_PATH).length() > 0);
	}

}