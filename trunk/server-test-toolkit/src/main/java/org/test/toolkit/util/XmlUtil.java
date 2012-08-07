package org.test.toolkit.util;

import org.dom4j.Document;

public final class XmlUtil {

	private XmlUtil() {
	}

	public static Document getDocument(String xmlPath) {
 		InputStream resourceAsStream = ConfigUtil.class.getClassLoader().getResourceAsStream(path);
		SAXReader saxReader = new SAXReader();
		Document document;
		try {
			document = saxReader.read(resourceAsStream);
		} catch (DocumentException e) {
 			e.printStackTrace();
			throw new UtilException("parse xml fail:"+path, e);
		}
		return document;
	}

}
