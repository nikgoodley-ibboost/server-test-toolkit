package org.test.toolkit.services.zookeeper.operations;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;

public class GetChildrenOperation extends AbstractZookeeperOperationWithWatcher<List<String>> {

	private final String path;

	public GetChildrenOperation(ZooKeeper zookeeper, String path, Watcher watcher) {
		super(zookeeper, watcher);
		this.path = path;
	}

	public GetChildrenOperation(ZooKeeper zookeeper, String path, boolean watch) {
		super(zookeeper, watch);
		this.path = path;
	}

	@Override
	public List<String> execute() throws KeeperException, InterruptedException {
		if (hasExplicitWatcher)
			return zookeeper.getChildren(path, watcher);

		return zookeeper.getChildren(path, watch);
	}

	@Override
	public String operationName() {
		return "getChildren path: " + path;
	}
}