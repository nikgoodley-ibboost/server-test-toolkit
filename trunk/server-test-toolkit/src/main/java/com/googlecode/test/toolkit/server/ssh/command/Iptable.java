package com.googlecode.test.toolkit.server.ssh.command;

/**
 * @author fu.jian
 * date Jul 26, 2012
 */
public class Iptable extends Command {

	private static final String IPTABLE_COMMAND_FORMAT_FOR_BLOCK_CONNECTIONS = "/sbin/iptables -t filter -A %s %s -j DROP";
	private static final String IPTABLE_COMMAND_FORMAT_FOR_CLEAR_RULES = "/sbin/iptables -F";

 	public static Iptable newInstanceForClearIptables() {
		return new Iptable(IPTABLE_COMMAND_FORMAT_FOR_CLEAR_RULES);
	}

	public static Iptable newInstanceForBlockConnections(boolean isOutput, String ip) {
		String forword = isOutput ? "INPUT -s" : "OUTPUT -d";
		String comandStr = String.format(IPTABLE_COMMAND_FORMAT_FOR_BLOCK_CONNECTIONS, forword, ip);

		return new Iptable(comandStr);
 	}

	private Iptable(String commandStr) {
		super(commandStr);
	}

}
