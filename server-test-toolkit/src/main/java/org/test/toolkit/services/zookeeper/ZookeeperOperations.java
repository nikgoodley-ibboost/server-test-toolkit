package org.test.toolkit.services.zookeeper;

import java.util.List;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.data.Stat;
import org.test.toolkit.services.zookeeper.operations.ZookeeperOperation;

public interface ZookeeperOperations {

	String createSequenceNode(String parentPath, String path, byte[] data) throws KeeperException,
			InterruptedException;

	String createEphemeralNode(String path, byte[] data) throws KeeperException, InterruptedException;

	String createPersistentNode(String path, byte data[]) throws KeeperException, InterruptedException;

	List<String> getChildren(String path, boolean watch) throws KeeperException, InterruptedException;

	List<String> getChildren(String path, Watcher watch) throws KeeperException, InterruptedException;

	Stat exists(String path, Watcher watcher) throws KeeperException, InterruptedException;

	Stat exists(String path, boolean watcher) throws KeeperException, InterruptedException;

	void delete(String path, int version) throws InterruptedException, KeeperException;

	Stat setData(String path, byte data[], int version) throws KeeperException, InterruptedException;

	void ensurePathExist(String path) throws KeeperException, InterruptedException;

	byte[] getData(String siblePath, boolean watch, Stat stat) throws KeeperException, InterruptedException;

	byte[] getData(String path, Watcher watcher, Stat stat) throws KeeperException, InterruptedException;

	<T> T executeZookeeperOperate(ZookeeperOperation<T> operation) throws KeeperException,
			InterruptedException;

}