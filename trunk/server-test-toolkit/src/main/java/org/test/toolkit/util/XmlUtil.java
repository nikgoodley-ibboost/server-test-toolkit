package org.test.toolkit.util;

import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;

public final class XmlUtil {

	private XmlUtil() {
	}

	public static Document getDocument(String xmlPath) {
 		InputStream resourceAsStream = XmlUtil.class.getClassLoader().getResourceAsStream(xmlPath);
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(resourceAsStream);
		} catch (DocumentException e) {
 			throw new RuntimeException("parse xml fail:"+xmlPath, e);
		}
		return document;
	}

}
