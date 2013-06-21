package org.test.toolkit.unittest.file;

import static org.junit.Assert.*;

import org.junit.Test;
import org.test.toolkit.file.RandomFile;
import org.test.toolkit.file.RandomFileFactory;

public class RandomFileFactoryTest {

	@Test
	public void testNewRandomFile() {
		
		RandomFile newRandomFileForTxt = RandomFileFactory.newRandomFileForTxt();
		newRandomFileForTxt.getFileContent();

 	}

	@Test
	public void testNewRandomFileForTxt() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewRandomFileForPdf() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewRandomFileForJpg() {
		fail("Not yet implemented");
	}

	@Test
	public void testNewRandomFileForTxtLong() {
		fail("Not yet implemented");
	}

}
