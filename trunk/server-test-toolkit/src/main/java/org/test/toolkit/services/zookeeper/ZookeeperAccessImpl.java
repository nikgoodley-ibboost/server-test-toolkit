package org.test.toolkit.services.zookeeper;

import java.io.IOException;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.test.toolkit.collections.Stack;
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

public class ZookeeperAccessImpl implements Watcher, ZookeeperAccess {

	private static final Logger LOG = Logger.getLogger(ZookeeperAccessImpl.class);
 	private static final int RETRY_TIMES = 2;

	protected volatile ZooKeeper zookeeper;

	private final String connectString;
	private final int sessionTimeout;
	private volatile long sessionId;

	public static ZookeeperAccess getInstance(String connectString, int sessionTimeout) {
		return new ZookeeperAccessImpl(connectString, sessionTimeout);
	}

	private ZookeeperAccessImpl(String connectString, int sessionTimeout) {
		LOG.info("Zookeeper config : connectStr=" + connectString + ", sessionTimeout=" + sessionTimeout);
		this.connectString = connectString;
		this.sessionTimeout = sessionTimeout;
		createZookeeper(0);
 	}

	private synchronized void createZookeeper(int count) {
		if (zookeeper == null) {
			zookeeper = createZK(sessionId);
			LOG.info("init zookeeper success.");
		} else {
			try {
				if (count < RETRY_TIMES) {
					LOG.info("The ZK server status is " + zookeeper.getState() + ", count: " + count
							+ ", retry again.");
					return;
				}
				if (isAvalable()) {
					LOG.info("The ZK server status is avalable, zk state is " + zookeeper.getState());
					return;
				}

				zookeeper.exists("/", false);

			} catch (KeeperException e) {
				if ((e instanceof KeeperException.SessionExpiredException)
						|| (e instanceof KeeperException.ConnectionLossException)) {
					try {
						LOG.info("Create ZK instance once more, zk state is " + zookeeper.getState()
								+ ", for ", e);

						// close old zookeer instance
						zookeeper.close();

						LOG.info("Close the older zk instance. its state is " + zookeeper.getState());
						zookeeper = createZK(sessionId);
						LOG.info("Create one zk instance. its state is " + zookeeper.getState());

					} catch (InterruptedException e1) {
						LOG.error("thread interrupted.", e1);
						Thread.currentThread().interrupt();
					}
				}
			} catch (InterruptedException e2) {
				LOG.error("thread interrupted.", e2);
				Thread.currentThread().interrupt();
			}
		}
	}

	protected <E> E executeZookeeperOperate(ZookeeperOperation<E> operation) throws KeeperException, InterruptedException {
		KeeperException exception = null;
		for (int i = 1; i <= RETRY_TIMES; i++) {
			try {
				return operation.execute();
			} catch (KeeperException e) {
				if ((e instanceof KeeperException.SessionExpiredException)
						|| (e instanceof KeeperException.ConnectionLossException)) {

					createZookeeper(i);
					exception = e;
				} else {
					exception = e;
				}
			}
		}
		throw exception;
	}



	private ZooKeeper createZK(final long sessionId) {
		ZooKeeper zk = null;
		try {
			LOG.info("-------create zookeeper instance starting--------");
			zk = new ZooKeeper(connectString, sessionTimeout, this);
			LOG.info("-------create zookeeper instance success--------");
			return zk;
		} catch (IOException e) {
			LOG.error("-------create zookeeper instance fail--------", e);
			return null;
		} finally {
			if (zk != null) {
				this.sessionId = zk.getSessionId();
			}
		}
	}

	@Override
	public String createSequenceNode(final String parentPath, final String path, final byte[] data)
			throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new CreateSequenceNodeOperation(zookeeper,path, parentPath, data));
	}

	@Override
	public String createEphemeralNode(final String path, final byte[] data) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new CreateNode(zookeeper,path, data,CreateMode.EPHEMERAL));
	}

	@Override
	public String createPersistentNode(final String path, final byte data[]) throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new CreateNode(zookeeper,path, data,CreateMode.PERSISTENT));
	}

	@Override
	public List<String> getChildren(final String path, final boolean watch) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new ZookeeperOperation<List<String>>() {
			@Override
			public List<String> execute() throws KeeperException, InterruptedException {
				return zookeeper.getChildren(path, watch);
			}

			@Override
			public String operationName() {
				return "getChildren path: " + path;
			}
		});
	}

	@Override
	public List<String> getChildren(final String path, final Watcher watch) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new GetChildrenOperation(zookeeper, path, watch));
	}

	@Override
	public long getSessionId() {
		return zookeeper.getSessionId();
 	}

	@Override
	public boolean isAvalable() {
		if (zookeeper.getState() == ZooKeeper.States.CONNECTED) {
			return true;
		} else {
			boolean normal = true;
			try {
				zookeeper.exists("/", false);
			} catch (Exception e) {
				normal = false;
			}
			return normal;
		}
	}

	@Override
	public Stat exists(final String path, final Watcher watcher) throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new ExistOperation(zookeeper,path, watcher));
	}

	@Override
	public Stat exists(final String path, final boolean watcher) throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new ZookeeperOperation<Stat>() {
			@Override
			public Stat execute() throws KeeperException, InterruptedException {
				return zookeeper.exists(path, watcher);
			}

			@Override
			public String operationName() {
				return "exist path: " + path;
			}
		});
	}

	@Override
	public void delete(final String path, final int version) throws InterruptedException, KeeperException {
		executeZookeeperOperate(new DeleteOperation(zookeeper,path, version));
	}

	@Override
	public Stat setData(final String path, final byte data[], final int version) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new SetDataOperation(zookeeper,data, version, path));
	}

	@Override
	public void ensurePathExists(final String path) throws KeeperException, InterruptedException {
		Stat state = exists(path, false);
		if (state != null) {
			return;
		}

		String tmpPath=PathUtil.formatPath(path);
		Stack<String> unCreatedPathStack = getUncreatedPath(tmpPath);
		while (!unCreatedPathStack.empty()) {
			try {
				createPersistentNode(unCreatedPathStack.pop(), null);
			} catch (KeeperException.NodeExistsException e) {
			}
		}
 	}

	private Stack<String> getUncreatedPath(String path) throws KeeperException,
			InterruptedException {
		Stat state;
		Stack<String> unCreatedPathStack = new Stack<String>();
		unCreatedPathStack.push(path);
		int lastSlashPos = path.lastIndexOf(MarkConstants.SPLIT);
		while (lastSlashPos != 0) {
			path = path.substring(0, lastSlashPos);
 			state = exists(path, false);
			if (state != null) {
				break;
			}
 			unCreatedPathStack.push(path);
			lastSlashPos = path.lastIndexOf(MarkConstants.SPLIT);
		}
		return unCreatedPathStack;
	}

 	@Override
	public byte[] getData(final String siblePath, final boolean watch, final Stat stat)
			throws KeeperException, InterruptedException {
		return executeZookeeperOperate(new GetDataOperation(zookeeper,siblePath, stat, watch));
	}

	@Override
	public byte[] getData(final String path, final Watcher watcher, final Stat stat) throws KeeperException,
			InterruptedException {
		return executeZookeeperOperate(new ZookeeperOperation<byte[]>() {
			@Override
			public byte[] execute() throws KeeperException, InterruptedException {
				return zookeeper.getData(path, watcher, stat);
			}

			@Override
			public String operationName() {
				return "get data path: " + path;
			}
		});
	}

 	@Override
	public String getConnectStr() {
		return this.connectString;
	}

 	@Override
	public void process(WatchedEvent event) {
		LOG.info("default wather event: " + event);
	}
}
