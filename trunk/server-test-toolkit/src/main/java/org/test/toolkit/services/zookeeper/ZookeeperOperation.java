package org.test.toolkit.services.zookeeper;

import org.apache.zookeeper.KeeperException;

public interface ZookeeperOperation<E> {
	
	E execute() throws KeeperException, InterruptedException;

	String operationName();
	
}