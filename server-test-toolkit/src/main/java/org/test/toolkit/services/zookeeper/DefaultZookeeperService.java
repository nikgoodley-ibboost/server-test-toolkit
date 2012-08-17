package org.test.toolkit.services.zookeeper;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.test.toolkit.services.exception.ServiceExecuteException;
import org.test.toolkit.services.zookeeper.operations.ZookeeperOperation;

/**
 * @author fu.jian
 * date Aug 15, 2012
 */
public class DefaultZookeeperService extends AbstractZookeeperService implements Watcher {

	private static final Logger LOGGER = Logger.getLogger(DefaultZookeeperService.class);

	public static ZookeeperOperations getInstance(String connectString, int sessionTimeout) {
		return new DefaultZookeeperService(connectString, sessionTimeout);
	}

	private DefaultZookeeperService(String connectString, int sessionTimeout){
		super(connectString, sessionTimeout);
	}

	public ZooKeeper createZookeeper(String connectString, int sessionTimeout, Watcher watcher)
			throws IOException {
		return new ZooKeeper(connectString, sessionTimeout, watcher);
	}

	@Override
	public void process(WatchedEvent event) {
		LOGGER.info("proccess default wather event: " + event);
	}

	@Override
	public <E> E executeZookeeperOperate(ZookeeperOperation<E> operation){
		try {
			return operation.execute();
		} catch (Exception e) {
			throw new ServiceExecuteException(e.getMessage(),e);
 		}
	}

}
