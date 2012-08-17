package org.test.toolkit.services.zookeeper.operations;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

/**
 * @author fu.jian
 * date Aug 15, 2012
 */
public class GetDataOperation extends AbsZookeeperOperationWithWatcher<byte[]> {

	private final String path;
	private final Stat stat;

	public GetDataOperation(ZooKeeper zookeeper, String path, boolean watch, Stat stat) {
		super(zookeeper, watch);
		this.path = path;
		this.stat = stat;
	}

	public GetDataOperation(ZooKeeper zookeeper, String path, Watcher watcher, Stat stat) {
		super(zookeeper, watcher);
		this.path = path;
		this.stat = stat;
	}

	@Override
	public byte[] execute() throws KeeperException, InterruptedException {
		if (hasExplicitWatcher)
			return zookeeper.getData(path, watcher, stat);
		
		return zookeeper.getData(path, watch, stat);
	}

	@Override
	public String operationName() {
		return "get data path: " + path;
	}
}