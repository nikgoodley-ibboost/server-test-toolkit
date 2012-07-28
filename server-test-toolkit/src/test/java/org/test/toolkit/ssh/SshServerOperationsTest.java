package org.test.toolkit.ssh;

import java.util.Map;
import java.util.concurrent.TimeoutException;

import junit.framework.Assert;

import org.junit.Test;
import org.test.toolkit.server.ssh.ServerOperations;
import org.test.toolkit.server.ssh.SshServerOperations;
import org.test.toolkit.server.ssh.command.Ls;
import org.test.toolkit.server.ssh.exception.UncheckedServerOperationException;
import org.test.toolkit.server.user.SshUser;


public class SshServerOperationsTest {

	@Test
	public void testExecuteInnerCommandSingleServer() throws UncheckedServerOperationException,
			TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.95.207", 333, "logs", "pass");

		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne);
		Map<String, String> lsResult = serverOperations.ls("/usr");
		System.out.println(lsResult);
		Assert.assertNotNull(lsResult);
	}

	@Test
	public void testExecuteInnerCommand() throws UncheckedServerOperationException, TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.95.207", 333, "logs", "pass");
		SshUser sshUserTwo = new SshUser("10.224.95.209", 333, "logs", "pass");
		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne, sshUserTwo);
		Map<String, String> lsResult = serverOperations.ls("/usr");
		System.out.println(lsResult);
		Assert.assertNotNull(lsResult);
	}

	@Test
	public void testDirectlyExecuteCommandWithResult() throws UncheckedServerOperationException,
			TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.95.207", 333, "logs", "pass");
		SshUser sshUserTwo = new SshUser("10.224.95.209", 333, "logs", "pass");
		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne, sshUserTwo);

		Map<String, String> executeCommandWithResult = serverOperations.executeCommandWithResult(Ls
				.newInstanceLsPath("/usr"));
		System.out.println(executeCommandWithResult);
		Assert.assertNotNull(executeCommandWithResult);

	}

	@Test
	public void testDirectlyExecuteCommandWithoutResult() throws UncheckedServerOperationException,
			TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.95.207", 333, "logs", "pass");
		SshUser sshUserTwo = new SshUser("10.224.95.209", 333, "logs", "pass");
		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne, sshUserTwo);

		serverOperations.executeCommandWithoutResult(Ls.newInstanceLsPath("/usr"));
	}

	@Test
	public void testDirectlyExecuteCommandWithError() throws UncheckedServerOperationException,
			TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.95.207", 333, "logs", "pass");

		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne);
		Exception e=null;
		try {
			Map<String, String> executeCommandWithResult = serverOperations.executeCommandWithResult(Ls
					.newInstanceLsPath("/"));
			System.out.println(executeCommandWithResult);
			Assert.assertNotNull(executeCommandWithResult);
		} catch (Exception e1) {
			e=e1;
		} finally {
			serverOperations.close();
 		}
		Assert.assertNotNull(e);
 	}

	@Test
	public void testExecuteInnerGetLineCommand() throws UncheckedServerOperationException, TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.57.207", 333, "root", "Aa1234");
//		SshUser sshUserTwo = new SshUser("10.224.95.209", 333, "logs", "pass");
		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne);
		Map<String, Long> lsResult = serverOperations.getLineNumberForFile("/usr/local/nginx/conf/idmap.conf");
		System.out.println(lsResult);
		Assert.assertNotNull(lsResult);
	}

	@Test
	public void testExecuteInnerGrepCommand() throws UncheckedServerOperationException, TimeoutException {
		SshUser sshUserOne = new SshUser("10.224.57.207", 333, "root", "Aa1234");
//		SshUser sshUserTwo = new SshUser("10.224.95.209", 333, "logs", "pass");
		ServerOperations serverOperations = SshServerOperations.getInstance(sshUserOne);
		Map<String, String> lsResult = serverOperations.getContentWithKeywords("/usr/local/nginx/conf/idmap.conf", "Timeou","5");
		System.out.println(lsResult);
		Assert.assertNotNull(lsResult);
	}

}
