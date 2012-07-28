package org.test.toolkit.server.ssh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.exception.ServerConnectionException;
import org.test.toolkit.server.common.exception.ServerTimeoutException;
import org.test.toolkit.server.common.exception.UncheckedServerOperationException;
import org.test.toolkit.server.common.user.SshUser;
import org.test.toolkit.util.CollectionUtil;
import org.test.toolkit.util.ValidationUtil;

import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author fu.jian
 * @date Jul 25, 2012
 */
public class SshServerOperations extends AbstractServerOperations {

	private static final int COMMAND_EXECUTE_TIME_OUT_SECONDS = 60;

	private static final Logger LOGGER = Logger.getLogger(SshServerOperations.class);

	private static volatile Map<String, Session> ipSessionMap = new ConcurrentHashMap<String, Session>();
	private static ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();

	private SshServerOperations(SshUser atLeastOneSshUser, SshUser... otherSshUsers) {
		ValidationUtil.nonNull(atLeastOneSshUser);

		List<SshUser> allSshUsers = CollectionUtil.getList(atLeastOneSshUser, otherSshUsers);
 		for (SshUser sshUser : allSshUsers) {
			String host = sshUser.getHost();
			if (!ipSessionMap.containsKey(host))
				synchronized (host.intern()) {
					if (!ipSessionMap.containsKey(host)) {
						Session session = getSshSession(sshUser);
						ipSessionMap.put(host, session);
					}
				}
		}

		LOGGER.info("[Server] [Complete Init IP-Session Map] " + ipSessionMap);
	}

	public static SshServerOperations getInstance(SshUser atLeaseOneSshUser, SshUser... otherSshUsers)
			throws UncheckedServerOperationException {
		return new SshServerOperations(atLeaseOneSshUser, otherSshUsers);
	}

	private Session getSshSession(SshUser sshUser) {
		LOGGER.info("[Server] [Create connnection] [begin] with: " + sshUser);

		Session session;
		JSch jSch = new JSch();
		try {
			session = jSch.getSession(sshUser.getUsername(), sshUser.getHost(), sshUser.getPort());
			session.setPassword(sshUser.getPassword());
			setConfigForSession(session);
			session.connect();
			LOGGER.info("[Server] [Create connnection] [End] [Success]: " + sshUser);
		} catch (JSchException e) {
			String errorMsg = String.format(
					"[Server] [Create connnection] [End] [Fail] with: [%s] for: [%s]", sshUser,
					e.getMessage());
			ServerConnectionException serverConnectionException = new ServerConnectionException(errorMsg, e);
			LOGGER.error(errorMsg, serverConnectionException);

			throw serverConnectionException;
		}
		return session;
	}

	private void setConfigForSession(Session session) {
		Properties config = new Properties();
		config.setProperty("StrictHostKeyChecking", "no");
		session.setConfig(config);
	}

	@Override
	public Map<String, String> executeCommand(String command, boolean returnResult) {
		ValidationUtil.effectiveStr(command);

		Collection<SshTask> commandTasks = formatCommandTasks(command, returnResult);
		return invokeCommandTasks(commandTasks);
	}

	private Collection<SshTask> formatCommandTasks(String command, boolean returnResult) {
		int initialCapacity = ipSessionMap.size();
		Collection<SshTask> commandTasks = new ArrayList<SshTask>(initialCapacity);
		for (Entry<String, Session> ipSession : ipSessionMap.entrySet()) {
			Session session = ipSession.getValue();
			commandTasks.add(new SshTask(session, command, returnResult));
		}

		return commandTasks;
	}

	private Map<String, String> invokeCommandTasks(Collection<SshTask> commandTasks) {
		Map<String, String> resultMap = new HashMap<String, String>();
		List<Future<OperationResult<String, String>>> futures = new ArrayList<Future<OperationResult<String, String>>>();
		try {
			futures = newCachedThreadPool.invokeAll(commandTasks, COMMAND_EXECUTE_TIME_OUT_SECONDS,
					TimeUnit.SECONDS);
			for (Future<OperationResult<String, String>> future : futures) {
				if (future.isCancelled()) {
					String message = String.format(" not return after: [%d][%s]",
							COMMAND_EXECUTE_TIME_OUT_SECONDS, TimeUnit.SECONDS.toString());
					throw new ServerTimeoutException(message);
				}
				OperationResult<String, String> operationResult = future.get();
				resultMap.put(operationResult.getKey(), operationResult.getValue());
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			throw new CommandExecuteException(e.getMessage(), e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if(cause!=null&&cause instanceof UncheckedServerOperationException)
				throw (UncheckedServerOperationException)cause;
			LOGGER.error(e.getMessage(), e);
			throw new UncheckedServerOperationException(e.getMessage(), e);
		}

		return resultMap;
	}

	@Override
	public void close() {
		LOGGER.info("[Server] [Release connnection] " + ipSessionMap.keySet().toString());

		clearConnectionMap();
		releaseConnections();
	}

	private void clearConnectionMap() {
		ipSessionMap.clear();
	}

	private void releaseConnections() {
		for (Session session : ipSessionMap.values())
			session.disconnect();
	}

	@Override
	public String toString() {
		return ipSessionMap.toString();
	}

}
