package org.test.toolkit.util;

import org.test.toolkit.constants.MarkConstants;

public final class StringUtil {

	/**
	 * to format such as "stringOne stringTwo stringThird" which takes the
	 * {@value MarkConstants#SPACE} as the spliter.
	 * 
	 * @param strings
	 * @return
	 */
	public static String concatWithSpace(String... strings) {
		return concat(MarkConstants.SPACE, strings);
	}

	private static String concat(String spliter, String... strings) {
		ValidationUtil.checkNull(strings);
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
			stringBuffer.append(spliter);
		}

		return stringBuffer.toString().trim();
	}

	/**
	 * concat all strings directly
	 * 
	 * @param strings
	 * @return
	 */
	public static String concatDirectly(String... strings) {
		return concat(MarkConstants.EMPTY_STRING, strings);

	}

	/**
	 * concat all strings with {@value MarkConstants#SEMICOLON}
	 * 
	 * @param strings
	 * @return
	 */
	public static String concatWithSemicolon(String... strings) {
		return concat(MarkConstants.SEMICOLON, strings);

	}

	private StringUtil() {
	}
}
