package org.test.toolkit.server.ssh.command;

import java.util.HashMap;

import org.test.toolkit.server.ssh.performance.PerformanceData;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class Vmstat extends Command {

	private static final String VMSTAT_COMMAND_FORMAT = "vmstat";

	private Vmstat(String commandStr) {
		super(commandStr);
	}

	public static Vmstat newInstance() {
		return new Vmstat(VMSTAT_COMMAND_FORMAT);
	}

	public static PerformanceData parseString(String str) {
		int startIndexForKeys = str.indexOf(" r");
		int endIndexForKeys = str.indexOf(" st") + 3;
		String[] keys = subStrAndSplitWithSpace(str, startIndexForKeys, endIndexForKeys);

		int startIndexForValues = endIndexForKeys;
		int endIndexForValues = str.length();
		String[] values = subStrAndSplitWithSpace(str, startIndexForValues, endIndexForValues);

		HashMap<String, Integer> hashMap = new HashMap<String, Integer>();
		for (int i = 0; i < keys.length; i++) {
			hashMap.put(keys[i], Integer.valueOf(values[i]));
		}

		return PerformanceData.fromMap(hashMap);
	}

	private static String[] subStrAndSplitWithSpace(String str, int startIndex, int endIndex) {
		String rawKeys = str.substring(startIndex, endIndex);
		String[] keys = rawKeys.trim().split("(\\s)+");
		return keys;
	}

}
