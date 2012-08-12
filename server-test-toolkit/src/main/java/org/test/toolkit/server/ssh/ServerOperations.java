package org.test.toolkit.server.ssh;

import java.util.List;
import java.util.Map;

import org.test.toolkit.server.common.interfaces.RemoteConnection;
import org.test.toolkit.server.ssh.command.Command;
import org.test.toolkit.server.ssh.performance.PerformanceData;

/**
 * @author fu.jian
 * @date Jul 25, 2012
 */
public interface ServerOperations extends RemoteConnection {

	public Map<String, String> executeCommandWithResult(String command);

	public void executeCommandWithoutResult(String command);

	public Map<String, String> executeCommandWithResult(Command command);

	public void executeCommandWithoutResult(Command command);

	public void cp(String fromPath, String toPath);

	public Map<String, String> ls(String path);

	public Map<String, String> getContentWithKeywords(String path, String atLeastOneKeyword,
			String... otherKeywords);

	public void createFileWithoutContent(String fileName);

	public Map<String, String> getContentBetweenLines(String fullPath, int startLine, int endLine,
			String... keywords);

	public Map<String, String> getContentFromLine(String fullPath, long startline, String... keywords);

	public Map<String, Long> getLineNumberForFile(String filePath);

	public void blockConnectionsWithIp(String atLeastOneIp, String... otherIps);

	public void blockConnectionsWithIp(boolean isOutput, String atLeastOneIp, String... otherIps);

	public void modifyFile(String path, int lineNumber, String newContentForLine, String backupPath);

	public Map<String, List<String>> getLineNumbersWithKeyword(String filePath, String keyword);

	public void createPath(String path);

	public void cancelIptables();

	public Map<String, PerformanceData> getPerformanceData();

}