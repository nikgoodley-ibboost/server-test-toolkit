package org.test.toolkit.util;

import org.test.toolkit.constants.MarkConstants;


public final class StringUtil {

	private StringUtil() {
	}

	public static String concatWithSpace(String... strings) {
		ValidationUtil.nonNull((Object[]) strings);
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
			stringBuffer.append(MarkConstants.SPACE);
		}

  		return stringBuffer.toString().trim();
	}

	public static String concat(String... strings) {
 		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
 		}

   		return stringBuffer.toString().trim();
	}

}
