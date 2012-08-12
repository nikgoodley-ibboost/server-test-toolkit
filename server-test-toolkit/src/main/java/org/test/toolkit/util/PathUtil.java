package org.test.toolkit.util;

import static org.test.toolkit.constants.MarkConstants.*;

/**
 * @author fu.jian
 */
public final class PathUtil {

	/**
	 * path formatted to : /xx/yy/zz
	 *
	 * @param path
	 * @return format such as: /xx/yy/zz
	 */
	public static String formatPath(String path) {
		ValidationUtil.checkString(path);
		String returnPath = path.trim();
		if (!returnPath.startsWith(SPLIT))
			returnPath = SPLIT + returnPath;
		if (returnPath.endsWith(SPLIT))
			returnPath = returnPath.substring(0, returnPath.length() - 1);

		return returnPath;
	}

	private PathUtil() {
	}
}
