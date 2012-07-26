package org.test.toolkit.ssh.command;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class Tail extends Command {
	
	private static final String TAIL_COMMAND_FORMAT = "tail -n+%d %s";

	private Tail(String commandStr) {
 		super(commandStr);
	}

	public static Tail newInstanceForGetContentFromLine(String path, long startline, String... keywords) {
		String commandStr = String.format(TAIL_COMMAND_FORMAT, startline, path);
		commandStr = CommandUtil.addAllGrepKey(commandStr, keywords);
		
		return new Tail(commandStr);
	}
 
}
