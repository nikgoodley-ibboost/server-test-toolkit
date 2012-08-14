package org.test.toolkit.services.zookeeper.operations;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;

public class DeleteOperation extends AbsZookeeperOperation<Object> {
	private final String path;
	private final int version;

	public DeleteOperation(ZooKeeper zookeeper,String path, int version) {
		super(zookeeper);
		this.path = path;
		this.version = version;
	}

	@Override
	public Object execute() throws KeeperException, InterruptedException {
		zookeeper.delete(path, version);
		return null;
	}

	@Override
	public String operationName() {
		return "delete path: " + path;
	}
}