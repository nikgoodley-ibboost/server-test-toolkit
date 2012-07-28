package org.test.toolkit.server.ssh.command;

import org.test.toolkit.util.ValidationUtil;

/**
 * @author fu.jian
 * @date Jul 26, 2012
 */
public class GroupCommands extends Command {

	private GroupCommands(Command atLeastOneCommand, Command... otherCommands) {
		super(toCommandStr(atLeastOneCommand, otherCommands));
	}

	private static String toCommandStr(Command atLeastOneCommand, Command... otherCommands) {
		ValidationUtil.nonNull(atLeastOneCommand);

		StringBuilder commandStr = new StringBuilder(atLeastOneCommand.toString());
		for (Command otherCommand : otherCommands) {
			commandStr.append(";");
			commandStr.append(otherCommand.toString());
		}

		return commandStr.toString();
	}

	public static GroupCommands newInstance(Command atLeastOneCommand, Command... otherCommands) {
		return new GroupCommands(atLeastOneCommand, otherCommands);
	}

}
