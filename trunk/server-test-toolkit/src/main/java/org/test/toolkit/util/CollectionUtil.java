package org.test.toolkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author fu.jian date Jul 26, 2012
 */
public final class CollectionUtil {

	public static <T> List<T> toList(T atLeastOneT, T... otherTs) {
		ArrayList<T> allTs = new ArrayList<T>(Arrays.asList(otherTs));
		allTs.add(atLeastOneT);

		return allTs;
	}

	/**
	 * merge map's value to string
	 * @param map
	 * @return
	 */
	public static <T> String mergeValues(Map<T, String> map) {
		Collection<String> values = map.values();

		StringBuffer stringBuffer = new StringBuffer();
		for (String value : values) {
			stringBuffer.append(value);
		}

		return stringBuffer.toString();
	}

	private CollectionUtil() {
	}

}
