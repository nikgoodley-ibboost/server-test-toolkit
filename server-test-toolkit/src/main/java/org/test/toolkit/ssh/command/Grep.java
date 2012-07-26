package org.test.toolkit.ssh.command;

import org.test.toolkit.util.ValidationUtil;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class Grep extends Command {

	private static final String GREP_COMMAND_FORMAT = "grep '%s' %s";

 	private Grep(String commandStr) {
		super(commandStr);
	}
	
	public static Grep newInstance(String path, String atLeastOneKeyword,
			String... otherKeywords) {
		return new Grep(toCommandStr(path, atLeastOneKeyword, otherKeywords));
	}

	private static String toCommandStr(String path, String keyWord,
			String... otherKeyWords) {
 		ValidationUtil.effectiveStr(keyWord,path);

		String commandStr = String.format(GREP_COMMAND_FORMAT, keyWord, path);
		return CommandUtil.addAllGrepKey(commandStr, otherKeyWords);

	}
  
}
