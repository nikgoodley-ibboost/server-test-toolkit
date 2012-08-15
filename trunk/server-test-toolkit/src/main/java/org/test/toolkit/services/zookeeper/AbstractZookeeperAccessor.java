package org.test.toolkit.services.zookeeper;

import java.io.IOException;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.test.toolkit.constants.MarkConstants;
import org.test.toolkit.services.zookeeper.operations.CreateNode;
import org.test.toolkit.services.zookeeper.operations.CreateSequenceNodeOperation;
import org.test.toolkit.services.zookeeper.operations.DeleteOperation;
import org.test.toolkit.services.zookeeper.operations.ExistOperation;
import org.test.toolkit.services.zookeeper.operations.GetChildrenOperation;
import org.test.toolkit.services.zookeeper.operations.GetDataOperation;
import org.test.toolkit.services.zookeeper.operations.SetDataOperation;
import org.test.toolkit.services.zookeeper.operations.ZookeeperOperation;
import org.test.toolkit.util.PathUtil;

public abstract class AbstractZookeeperAccessor implements ZookeeperOperations, ZookeeperConnection, Watcher {

	private static final Logger LOGGER = Logger.getLogger(DefaultZookeeperAccessor.class);

	protected String connectString;
	protected int sessionTimeout;
	protected ZooKeeper zookeeper;
	protected long sessionId;

	protected AbstractZookeeperAccessor(String connectString, int sessionTimeout) throws IOException {
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
		this.zookeeper = createZookeeper(connectString, sessionTimeout, this);
		this.sessionId = zookeeper.getSessionId();
	}
 
	@Override
	public String createSequenceNode(final String parentPath, final String path, final byte[] data)
			throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new CreateSequenceNodeOperation(zookeeper, path, parentPath, data));
	}

	public abstract <E> E executeZookeeperOperate(ZookeeperOperation<E> operation) throws KeeperException,
			InterruptedException;

	@Override
	public String createEphemeralNode(final String path, final byte[] data) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new CreateNode(zookeeper, path, data, CreateMode.EPHEMERAL));
	}

	@Override
	public String createPersistentNode(final String path, final byte data[]) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new CreateNode(zookeeper, path, data, CreateMode.PERSISTENT));
	}

	@Override
	public List<String> getChildren(final String path, final boolean watch) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new GetChildrenOperation(zookeeper, path, watch));
	}

	@Override
	public List<String> getChildren(final String path, final Watcher watcher) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new GetChildrenOperation(zookeeper, path, watcher));
	}

	@Override
	public Stat exists(final String path, final Watcher watcher) throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new ExistOperation(zookeeper, path, watcher));
	}

	@Override
	public Stat exists(final String path, final boolean watch) throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new ExistOperation(zookeeper, path, watch));
	}

	@Override
	public void delete(final String path, final int version) throws InterruptedException, KeeperException {
		executeZookeeperOperate(new DeleteOperation(zookeeper, path, version));
	}

	@Override
	public Stat setData(final String path, final byte data[], final int version) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new SetDataOperation(zookeeper, data, version, path));
	}

	@Override
	public byte[] getData(final String path, final boolean watch, final Stat stat) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new GetDataOperation(zookeeper, path, watch, stat));
	}

	@Override
	public byte[] getData(final String path, final Watcher watcher, final Stat stat) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new GetDataOperation(zookeeper, path, watcher, stat));
	}

	@Override
	public boolean isAvalable() {
		return zookeeper.getState() == ZooKeeper.States.CONNECTED;
	}

	@Override
	public void ensurePathExist(final String path) throws KeeperException, InterruptedException {
		Stat state = exists(path, false);
		if (state != null)
			return;

		String tmpPath = PathUtil.formatPath(path);
		Stack<String> unCreatedPathStack = getUncreatedPath(tmpPath);
		while (!unCreatedPathStack.empty()) {
			try {
				createPersistentNode(unCreatedPathStack.pop(), null);
			} catch (KeeperException.NodeExistsException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	private Stack<String> getUncreatedPath(String path) throws KeeperException, InterruptedException {
		Stat state;
		Stack<String> unCreatedPathStack = new Stack<String>();
		unCreatedPathStack.push(path);
		int lastSplitPos = path.lastIndexOf(MarkConstants.SPLIT);
		while (lastSplitPos != 0) {
			path = path.substring(0, lastSplitPos);
			state = exists(path, false);
			if (state != null) {
				break;
			}
			unCreatedPathStack.push(path);
			lastSplitPos = path.lastIndexOf(MarkConstants.SPLIT);
		}
		return unCreatedPathStack;
	}

	public String getConnectString() {
		return connectString;
	}

	public int getSessionTimeout() {
		return sessionTimeout;
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public long getSessionId() {
		return sessionId;
	}

}
