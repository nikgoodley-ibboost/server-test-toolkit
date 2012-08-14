package org.test.toolkit.services.zookeeper.operations;

import org.apache.zookeeper.ZooKeeper;

public abstract class AbsZookeeperOperation<T> implements ZookeeperOperation<T> {

	protected ZooKeeper zookeeper;

	protected AbsZookeeperOperation(ZooKeeper zookeeper) {
		super();
		this.zookeeper = zookeeper;
	}

	public ZooKeeper getZookeeper() {
		return zookeeper;
	}

	public void setZookeeper(ZooKeeper zookeeper) {
		this.zookeeper = zookeeper;
	}

}
