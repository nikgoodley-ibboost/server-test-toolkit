package com.googlecode.test.toolkit.server.ssh;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.googlecode.test.toolkit.server.common.exception.CommandExecuteException;
import com.googlecode.test.toolkit.server.common.exception.ServerTimeoutException;
import com.googlecode.test.toolkit.server.common.exception.UncheckedServerOperationException;
import com.googlecode.test.toolkit.server.common.user.ServerUser;
import com.googlecode.test.toolkit.server.common.user.SshUser;
import com.googlecode.test.toolkit.server.common.util.JSchUtil.JSchSessionUtil;
import com.googlecode.test.toolkit.util.CollectionUtil;
import com.googlecode.test.toolkit.util.ValidationUtil;

/**
 * @author fu.jian
 * date Jul 25, 2012
 */
public class SshServerOperations extends AbstractServerOperations {

	private static final Logger LOGGER = Logger.getLogger(SshServerOperations.class);

	private ExecutorService newCachedThreadPool = Executors.newCachedThreadPool();
	private volatile Map<String, List<SessionWrapper>> ipSessionMap = new ConcurrentHashMap<String, List<SessionWrapper>>();
	private List<SshUser> allSshUsers;
	private int commandTimeOutTime = 60;


	/**
	 * after instance created, the connections will be created by default, but you should call
	 * {@link #disconnect()} to release the connections.
	 * @param atLeaseOneSshUser
	 * @param otherSshUsers
	 * @return SshServerOperations
	 * @throws UncheckedServerOperationException
	 */
	public SshServerOperations(SshUser atLeastOneSshUser, SshUser... otherSshUsers) {
  		allSshUsers = CollectionUtil.toList(atLeastOneSshUser, otherSshUsers);
 		ValidationUtil.checkNull(allSshUsers);
		connect();
	}

	@Override
	public void connect() {
		for (SshUser sshUser : allSshUsers) {
			String host = sshUser.getHost();
			if (isNotExistSessionForSshUser(host))
				synchronized (host.intern()) {
					if (isNotExistSessionForSshUser(host)) {
						SessionWrapper session = JSchSessionUtil.getSessionWrapper(sshUser);
						List<SessionWrapper> list=new ArrayList<SessionWrapper>();
						list.add(session);
						ipSessionMap.put(host, list);
					}
 				}
		}
		LOGGER.info("[Server] [Complete Init IP-Session Map] " + ipSessionMap);
	}

	private boolean isNotExistSessionForSshUser(String host) {
		return !ipSessionMap.containsKey(host)||(ipSessionMap.get(host)!=null&&ipSessionMap.get(host).size()==0);
	}

	public int getCommandTimeOutTime() {
		return commandTimeOutTime;
	}

	public void setCommandTimeOutTime(int commandTimeOutTime) {
		this.commandTimeOutTime = commandTimeOutTime;
	}

	public List<SshUser> getAllSshUsers() {
		return allSshUsers;
	}

	@Override
	public void executeCommandHanged(String command) {
		executeCommand(command, false, true);
	}

 	@Override
	public Map<String, String> executeCommand(String command, boolean returnResult, boolean isHanged) {
		ValidationUtil.checkString(command);

		Collection<SshTask> sshTasks = formatSshTasks(command, returnResult, isHanged);
		return invokeSshTasks(sshTasks);
	}

	private synchronized Collection<SshTask> formatSshTasks(String command, boolean returnResult, boolean isHanged) {
		int initialCapacity = ipSessionMap.size();
		ValidationUtil.checkPositive(initialCapacity);

		Collection<SshTask> sshTasks = new ArrayList<SshTask>(initialCapacity);
		for (Entry<String, List<SessionWrapper>> ipSession : ipSessionMap.entrySet()) {
			List<SessionWrapper> sessionWrappers = ipSession.getValue();
  			SessionWrapper availableSessionWrapper = getAvailableSessionWrapper(sessionWrappers);
  			availableSessionWrapper.increaseUsingChannelNumber();
			sshTasks.add(new SshTask(availableSessionWrapper, command, returnResult, isHanged));

		}

 		return sshTasks;
	}

	public SessionWrapper getAvailableSessionWrapper(List<SessionWrapper> sessionWrappers){
		for(SessionWrapper sessionWrapper:sessionWrappers){
			if(sessionWrapper.isSessionAvailable()){
				return sessionWrapper;
			}

		}

		ServerUser serverUser = sessionWrappers.get(0).getServerUser();
		SessionWrapper sessionWrapper = JSchSessionUtil.getSessionWrapper(serverUser);
		ipSessionMap.get(serverUser.getHost()).add(sessionWrapper);

		return sessionWrapper;
	}



	private Map<String, String> invokeSshTasks(Collection<SshTask> commandTasks) {
		Map<String, String> resultMap = new HashMap<String, String>();
		List<Future<SshTaskResult<String, String>>> futures = new ArrayList<Future<SshTaskResult<String, String>>>();
		try {
			futures = newCachedThreadPool.invokeAll(commandTasks, commandTimeOutTime,
					TimeUnit.SECONDS);
			for (Future<SshTaskResult<String, String>> future : futures) {
				if (future.isCancelled()) {
					String message = String.format(" not return after: [%d][%s]",
							commandTimeOutTime, TimeUnit.SECONDS.toString());
					throw new ServerTimeoutException(message);
				}
				SshTaskResult<String, String> operationResult = future.get();
				resultMap.put(operationResult.getHost(), operationResult.getResult());
			}
		} catch (InterruptedException e) {
			LOGGER.error(e.getMessage(), e);
			throw new CommandExecuteException(e.getMessage(), e);
		} catch (ExecutionException e) {
			Throwable cause = e.getCause();
			if (cause != null && cause instanceof UncheckedServerOperationException)
				throw (UncheckedServerOperationException) cause;
			LOGGER.error(e.getMessage(), e);
			throw new UncheckedServerOperationException(e.getMessage(), e);
		}

		return resultMap;
	}

	@Override
	public void disconnect() {
		LOGGER.info("[Server] [Release connnection] " + ipSessionMap.keySet().toString());

		disconnectSessions();
        clearConnectionMap();
	}

	private void clearConnectionMap() {
		ipSessionMap.clear();
	}

	private void disconnectSessions() {
		for (List<SessionWrapper> sessionWrapperList : ipSessionMap.values())
			for(SessionWrapper sessionWrapper:sessionWrapperList)
			JSchSessionUtil.disconnect(sessionWrapper.getSession());
	}

	@Override
	public String toString() {
		return ipSessionMap.toString();
	}


}
