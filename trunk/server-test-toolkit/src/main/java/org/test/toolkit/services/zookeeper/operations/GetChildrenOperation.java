package org.test.toolkit.services.zookeeper.operations;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class GetChildrenOperation extends AbsZookeeperOperation<List<String>> {

	private final String path;
	private final Watcher watch;

	public GetChildrenOperation(ZooKeeper zookeeper,String path, Watcher watch) {
		super(zookeeper);
		this.path = path;
		this.watch = watch;
	}

	@Override
	public List<String> execute() throws KeeperException, InterruptedException {
		return zookeeper.getChildren(path, watch);
	}

	@Override
	public String operationName() {
		return "getChildren path: " + path;
	}
}