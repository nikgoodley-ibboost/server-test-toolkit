package org.test.toolkit.file;

import java.io.ByteArrayOutputStream;

import org.apache.commons.io.IOUtils;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

public class PdfFile extends RandomFile {

	public static final String EXTENSION = FileType.PDF.toString();

	private static byte[] getContentBytes() {
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		try {
			Document doc = new Document();
			try {
				PdfWriter.getInstance(doc, byteArrayOutputStream);
				doc.open();
				doc.add(new Paragraph(RandomFile.newContent()));
			} finally {
				if (doc != null)
					doc.close();
			}

			return byteArrayOutputStream.toByteArray();
		} catch (DocumentException e) {
			throw new RandomFileException(e.getMessage(), e);
		} finally {
			IOUtils.closeQuietly(byteArrayOutputStream);
		}
	}

	public PdfFile() {
		super(EXTENSION, getContentBytes());
	}

}
