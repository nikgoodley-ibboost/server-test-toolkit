package org.test.toolkit.util;

import org.test.toolkit.constants.MarkConstants;

public final class StringUtil {

	/**
	 * to format such as "stringOne stringTwo stringThird" which takes the space
	 * as the spliter.
	 * 
	 * @param strings
	 * @return
	 */
	public static String concatWithSpace(String... strings) {
		ValidationUtil.checkNull(strings);
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
			stringBuffer.append(MarkConstants.SPACE);
		}

		return stringBuffer.toString().trim();
	}

	/**
	 * concat all strings directly.
	 * 
	 * @param strings
	 * @return
	 */
	public static String concatDirectly(String... strings) {
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
		}

		return stringBuffer.toString().trim();
	}

	private StringUtil() {
	}
}
