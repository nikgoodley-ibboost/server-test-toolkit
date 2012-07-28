package org.test.toolkit.server.ssh.command;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class Rm extends Command {

	private static final String RM_COMMAND_FORMAT = "rm -f %s";
 
	private Rm(String commandStr) {
		super(commandStr);
	}
 
	public static Rm newInstance(String deletePath) {
		String commandStr = String.format(RM_COMMAND_FORMAT, deletePath);
		return new Rm(commandStr);
	}

}
