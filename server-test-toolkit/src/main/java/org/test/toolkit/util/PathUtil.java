package org.test.toolkit.util;

import static org.test.toolkit.constants.MarkConstants.*;

public final class PathUtil {

	private PathUtil() {
	}

	public static String formatPath(String path) {
		ValidationUtil.effectiveStr(path);
		String returnPath = path.trim();
		if (!returnPath.startsWith(SPLIT))
			returnPath = SPLIT + returnPath;
		if (returnPath.endsWith(SPLIT))
			returnPath = returnPath.substring(0, returnPath.length() - 1);

		return returnPath;
	}

}
