package org.test.toolkit.services.zookeeper.operations;

import java.util.List;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;

public class CreateSequenceNodeOperation extends AbstractZookeeperOperation<String> {

	private static final List<ACL> ACL = ZooDefs.Ids.OPEN_ACL_UNSAFE;

	private final String path;
	private final String parentPath;
	private final byte[] data;

	public CreateSequenceNodeOperation(ZooKeeper zookeeper, String path, String parentPath,
			byte[] data) {
		super(zookeeper);
		this.path = path;
		this.parentPath = parentPath;
		this.data = data;
	}

	@Override
	public String execute() throws KeeperException, InterruptedException {
		if (null == zookeeper.exists(parentPath, false)) {
			zookeeper.create(parentPath, null, ACL, CreateMode.PERSISTENT);
		}
		return zookeeper.create(path, data, ACL, CreateMode.EPHEMERAL_SEQUENTIAL);
	}

	@Override
	public String getOperationName() {
		return "createSequenceNode path " + path;
	}
}