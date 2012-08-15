package org.test.toolkit.services.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.test.toolkit.server.common.exception.ServerConnectionException;
import org.test.toolkit.services.exception.ServiceExecuteException;
import org.test.toolkit.services.exception.ServiceTimeoutException;
import org.test.toolkit.util.CollectionUtil;
import org.test.toolkit.util.ValidationUtil;

public class MemcachedAccessorImpl implements MemcachedAccessor {

	private final MemcachedClient memcachedClient;

	public MemcachedAccessorImpl(InetSocketAddress atLeastOneInetSocketAddress,
			InetSocketAddress... otherInetSocketAddresses) {
		ValidationUtil.checkNull(atLeastOneInetSocketAddress);
		ValidationUtil.checkNull(otherInetSocketAddresses);

		List<InetSocketAddress> list = CollectionUtil.getList(
				atLeastOneInetSocketAddress, otherInetSocketAddresses);
		try {
			memcachedClient = new MemcachedClient(list);
		} catch (IOException e) {
			throw new ServerConnectionException(e.getMessage(), e);
		}
	}

	/**
	 * @param inetSocketAddressString
	 *            : "host:port host2:port2"
	 * @throws IOException
	 */
	public MemcachedAccessorImpl(String inetSocketAddressString) {
		ValidationUtil.checkString(inetSocketAddressString);
		try {
			memcachedClient = new MemcachedClient(
					AddrUtil.getAddresses(inetSocketAddressString));
		} catch (IOException e) {
			throw new ServerConnectionException(e.getMessage(), e);
		}
	}

	@Override
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	@Override
	public void set(String key, Object value, int cacheTime, long timeout,
			TimeUnit timeUnit) {
		Future<Boolean> future = memcachedClient.set(key, cacheTime, value);
		try {
			future.get(timeout, timeUnit);
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new ServiceTimeoutException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ServiceExecuteException(e.getMessage(), e);
		}
	}

	@Override
	public Object get(String key) {
		return memcachedClient.get(key);
	}

	@Override
	public Object asyncGet(String key, long timeout, TimeUnit timeUnit) {
		Future<Object> f = memcachedClient.asyncGet(key);
		try {
			Object value = f.get(timeout, timeUnit);
			return value;
		} catch (TimeoutException e) {
			f.cancel(true);
			throw new ServiceTimeoutException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ServiceExecuteException(e.getMessage(), e);
		}
	}

	@Override
	public Map<String, Object> getBulk(Collection<String> keys) {
		return memcachedClient.getBulk(keys);
	}

	@Override
	public Map<String, Object> asyncGetBulk(Collection<String> keys,
			long timeout, TimeUnit timeUnit) {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> myObj = null;
		Future<Map<String, Object>> future = memcachedClient.asyncGetBulk(keys);
		try {
			myObj = future.get(timeout, timeUnit);
			for (String key : keys) {
				returnMap.put(key, myObj.get(key));
			}
			return returnMap;
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new ServiceTimeoutException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ServiceExecuteException(e.getMessage(), e);
		}

	}

	@Override
	public List<String> deleteBulk(Collection<String> keys, long timeout,
			TimeUnit timeUnit) {
		List<String> failKeys = new ArrayList<String>();
		for (String key : keys) {
			try {
				delete(key, timeout, timeUnit);
			} catch (Exception e) {
				failKeys.add(key);
			}
		}
		return failKeys;
	}

	@Override
	public void delete(String key, long timeout, TimeUnit timeUnit) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			future.get(timeout, timeUnit);
		} catch (TimeoutException e) {
			future.cancel(true);
			throw new ServiceTimeoutException(e.getMessage(), e);
		} catch (Exception e) {
			throw new ServiceExecuteException(e.getMessage(), e);
		}
	}

	@Override
	public void flush() {
		memcachedClient.flush();
	}

	@Override
	public void shutdown() {
		if (memcachedClient != null) {
			memcachedClient.shutdown();
		}
	}
}
