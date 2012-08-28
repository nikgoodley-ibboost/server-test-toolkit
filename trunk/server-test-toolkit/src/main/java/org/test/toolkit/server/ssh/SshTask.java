package org.test.toolkit.server.ssh;

import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.Callable;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.test.toolkit.server.common.exception.CommandExecuteException;
import org.test.toolkit.server.common.exception.ContentOverSizeException;
import org.test.toolkit.server.common.exception.UncheckedServerOperationException;
import org.test.toolkit.server.common.util.JSchUtil.JSchChannelUtil;
import org.test.toolkit.util.MemoryUtil;
import org.test.toolkit.util.ValidationUtil;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;

/**
 * @author fu.jian
 * date Jul 25, 2012
 */
public class SshTask implements Callable<SshTaskResult<String, String>> {

	private static final Logger LOGGER = Logger.getLogger(SshTask.class);

	private String command;
	private boolean isReturnResult;
	private Session session;

	SshTask(Session session, String command, boolean returnResult) {
		ValidationUtil.checkString(command);
		ValidationUtil.checkNull(session);

		this.session = session;
		this.command = command;
		this.isReturnResult = returnResult;
	}

	@Override
	public SshTaskResult<String, String> call() throws Exception {
		LOGGER.info("[Server] [Execute command] [Begin] command is: (" + command + ")");

		InputStream inputStream = null;
		InputStream errStream = null;
		ChannelExec channelExec=null;
		try {
			channelExec=JSchChannelUtil.getExecChannel(session);
 			configChannelExec(channelExec);

 			channelExec.connect();

			inputStream = channelExec.getInputStream();
			errStream = channelExec.getErrStream();
			judgeIfCommandExecuteError(errStream);

			return getResult(inputStream);
		} catch (UncheckedServerOperationException e) {
			logError(e);
			throw e;
		} catch (Exception e) {
			logError(e);
 			throw new UncheckedServerOperationException(e.getMessage(),e);
		} finally {
			closeChannelAndStream(channelExec, inputStream, errStream);
		}
	}

	private void configChannelExec(ChannelExec channelExec) throws JSchException {
 		channelExec.setCommand(command);
		channelExec.setErrStream(System.err);
		channelExec.setInputStream(null);

 	}

	private void judgeIfCommandExecuteError(InputStream errStream) throws IOException {
		String errorString = IOUtils.toString(errStream);
		if (!errorString.isEmpty()) {
			LOGGER.error("[Server] [Execute command] [End] [Fail] " + errorString);
			throw new CommandExecuteException(errorString);
		}
	}

	/**
	 * @param inputStream
	 * @return SshTaskResult: host:ResultString Pair
	 * @throws IOException
	 */
	private SshTaskResult<String, String> getResult(InputStream inputStream) throws IOException {
		String hostAsKey = session.getHost();
		SshTaskResult<String, String> operationResult = new SshTaskResult<String, String>(hostAsKey, null);
		if (isReturnResult) {
			judgeIfOverSize(inputStream);
			operationResult.setResult(IOUtils.toString(inputStream));
		}
		LOGGER.info("[Server] [Execute command] [End] [Success] command is: (" + command + ")");

		return operationResult;
	}

	private void logError(Exception e) {
		String errorMsg = String.format("[Server] [Execute command] [End] [Fail] command (%s) for (%s)",
				command, e.getMessage());
 		LOGGER.error(errorMsg, e);
 	}

	private void judgeIfOverSize(InputStream inputStream) throws IOException {
		long availableMemory = MemoryUtil.getAvailableMemory();
		int available = inputStream.available();
		if (available > availableMemory) {
			String message = String.format("current available memory is [%d], but content is [%d]",
					availableMemory, available);
			throw new ContentOverSizeException(message);
		}
	}

	private void closeChannelAndStream(Channel channel, InputStream inputStream, InputStream errStream) {
		IOUtils.closeQuietly(inputStream);
		IOUtils.closeQuietly(errStream);
		JSchChannelUtil.disconnect(channel);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SshTask [host=");
		builder.append(session.getHost());
		builder.append(", command=");
		builder.append(command);
		builder.append(", isReturnResult=");
		builder.append(isReturnResult);
		builder.append("]");

		return builder.toString();
	}
}