package org.test.toolkit.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public final class CollectionUtil {

	public static <T> List<T> toList(T atLeastOneT, T... otherTs) {
		ArrayList<T> allTs = new ArrayList<T>(Arrays.asList(otherTs));
		allTs.add(atLeastOneT);

		return allTs;
	}

	private CollectionUtil() {
	}

}
