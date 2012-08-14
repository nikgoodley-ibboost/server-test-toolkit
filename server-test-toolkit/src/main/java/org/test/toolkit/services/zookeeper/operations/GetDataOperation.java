package org.test.toolkit.services.zookeeper.operations;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

public class GetDataOperation extends AbsZookeeperOperation<byte[]> {

	private final String siblePath;
	private final Stat stat;
	private final boolean watch;

	public GetDataOperation(ZooKeeper zookeeper,String siblePath, Stat stat, boolean watch) {
		super(zookeeper);
		this.siblePath = siblePath;
		this.stat = stat;
		this.watch = watch;
	}

	@Override
	public byte[] execute() throws KeeperException, InterruptedException {
		return zookeeper.getData(siblePath, watch, stat);
	}

	@Override
	public String operationName() {
		return "get data path: " + siblePath;
	}
}