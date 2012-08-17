package org.test.toolkit.server.ssh;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.test.toolkit.server.ssh.command.Command;
import org.test.toolkit.server.ssh.command.Cp;
import org.test.toolkit.server.ssh.command.Grep;
import org.test.toolkit.server.ssh.command.GroupCommandFactory;
import org.test.toolkit.server.ssh.command.GroupCommands;
import org.test.toolkit.server.ssh.command.Iptable;
import org.test.toolkit.server.ssh.command.Ls;
import org.test.toolkit.server.ssh.command.Mkdir;
import org.test.toolkit.server.ssh.command.Sed;
import org.test.toolkit.server.ssh.command.Tail;
import org.test.toolkit.server.ssh.command.Touch;
import org.test.toolkit.server.ssh.command.Vmstat;
import org.test.toolkit.server.ssh.performance.PerformanceData;
import org.test.toolkit.util.CollectionUtil;

/**
 * @author fu.jian
 * date Jul 25, 2012
 */
public abstract class AbstractServerOperations implements ServerOperations {

	private static final String REGEX = "\n";

	@Override
	public void cp(String toPath, String fromPath) {
		Command cp = Cp.newInstance(fromPath, toPath);
		executeCommandWithoutResult(cp);
	}

	@Override
	public void executeCommandWithoutResult(Command command) {
		executeCommandWithoutResult(command.getCommandStr());
	}

	@Override
	public void executeCommandWithoutResult(String command) {
		executeCommand(command, false);
	}

	abstract Map<String, String> executeCommand(String command, boolean returnResult);

	@Override
	public Map<String, String> ls(String path) {
		Command command = Ls.newInstanceLsPath(path);
		return executeCommandWithResult(command);
	}

	@Override
	public Map<String, String> getContentWithKeywords(String path, String atLeastOneKeyword,
			String... otherKeywords) {
		Command command = Grep.newInstance(path, atLeastOneKeyword, otherKeywords);
		return executeCommandWithResult(command);
	}

	@Override
	public void createFileWithoutContent(String fileName) {
		Command command = Touch.newInstance(fileName);
		executeCommandWithoutResult(command);
	}

	@Override
	public Map<String, PerformanceData> getPerformanceData() {
		Command command = Vmstat.newInstance();
		Map<String, String> result = executeCommandWithResult(command);
		Map<String, PerformanceData> returnMap = new HashMap<String, PerformanceData>(result.size());
		for (Map.Entry<String, String> entry : result.entrySet()) {
			returnMap.put(entry.getKey(), PerformanceData.parsePerformanceData(entry.getValue()));
		}

		return returnMap;
	}

	@Override
	public Map<String, String> getContentBetweenLines(String fullPath, int startLine, int endLine,
			String... keywords) {
		Command command = Sed.newInstanceForGetContentBetweenLines(fullPath, startLine, endLine, keywords);
		return executeCommandWithResult(command);
	}

	@Override
	public Map<String, String> getContentFromLine(String fullPath, long startline, String... keywords) {
		Command command = Tail.newInstanceForGetContentFromLine(fullPath, startline, keywords);
		return executeCommandWithResult(command);
	}

	@Override
	public Map<String, Long> getLineNumberForFile(String filePath) {
		Command sed = Sed.newInstanceForGetLineNumber(filePath);
		Map<String, String> tmpResultMap = executeCommandWithResult(sed);
		Map<String, Long> finalResultMap = new HashMap<String, Long>(tmpResultMap.size());
		for (Entry<String, String> entry : tmpResultMap.entrySet()) {
			String value = entry.getValue();
			String numberStr = value.substring(0, value.indexOf(REGEX));
			finalResultMap.put(entry.getKey(), Long.parseLong(numberStr));
		}

		return finalResultMap;
	}

	@Override
	public void blockConnectionsWithIp(String atLeastOneIp, String... otherIps) {
		blockConnectionsWithIp(true, atLeastOneIp, otherIps);
	}

	@Override
	public void blockConnectionsWithIp(boolean isOutput, String atLeastOneIp, String... otherIps) {
		List<String> allIps = CollectionUtil.toList(atLeastOneIp, otherIps);

		for (String ip : allIps) {
			Command command = Iptable.newInstanceForBlockConnections(isOutput, ip);
			executeCommandWithoutResult(command);
		}
	}

	@Override
	public void modifyFile(String path, int lineNumber, String newContentForLine, String backupPath) {
		GroupCommands groupCommands = GroupCommandFactory.changeFile(path, lineNumber, newContentForLine,
				backupPath);
		executeCommandWithoutResult(groupCommands);
	}

	@Override
	public Map<String, List<String>> getLineNumbersWithKeyword(String filePath, String keyword) {
		Command sed = Sed.newInstanceForGetLineNumbersWithKey(filePath, keyword);

		Map<String, String> tmpResultMap = executeCommandWithResult(sed);
		Map<String, List<String>> finalResultMap = new HashMap<String, List<String>>(tmpResultMap.size());
		for (Entry<String, String> entry : tmpResultMap.entrySet()) {
			String value = entry.getValue();
			List<String> list = Arrays.asList(value.split(REGEX));
			finalResultMap.put(entry.getKey(), list);
		}

		return finalResultMap;
	}

	@Override
	public void createPath(String path) {
		Command command = Mkdir.newInstance(path);
		executeCommandWithoutResult(command);
	}

	@Override
	public void cancelIptables() {
		Command command = Iptable.newInstanceForClearIptables();
		executeCommandWithoutResult(command);
	}

	@Override
	public Map<String, String> executeCommandWithResult(Command command) {
		return executeCommandWithResult(command.getCommandStr());
	}

	@Override
	public Map<String, String> executeCommandWithResult(String command) {
		return executeCommand(command, true);
	}
}
