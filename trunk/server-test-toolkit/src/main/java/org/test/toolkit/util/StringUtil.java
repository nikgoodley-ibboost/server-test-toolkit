package org.test.toolkit.util;


public final class StringUtil {

	public static final String SPACE=" ";

	private StringUtil() {
	}

	public static String concatWithSpace(String... strings) {
		ValidationUtil.nonNull((Object[]) strings);
		StringBuffer stringBuffer = new StringBuffer();
		for (String string : strings) {
			stringBuffer.append(string);
			stringBuffer.append(SPACE);
		}

 		return stringBuffer.toString();
	}

}
