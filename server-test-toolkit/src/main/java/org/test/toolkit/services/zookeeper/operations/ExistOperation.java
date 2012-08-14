package org.test.toolkit.services.zookeeper.operations;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class ExistOperation extends AbsZookeeperOperation<Stat> {
	private final String path;
	private final Watcher watcher;

	public ExistOperation(ZooKeeper zookeeper,String path, Watcher watcher) {
		super(zookeeper);
		this.path = path;
		this.watcher = watcher;
	}

	@Override
	public Stat execute() throws KeeperException, InterruptedException {
		return zookeeper.exists(path, watcher);
	}

	@Override
	public String operationName() {
		return "exist path: " + path;
	}
}