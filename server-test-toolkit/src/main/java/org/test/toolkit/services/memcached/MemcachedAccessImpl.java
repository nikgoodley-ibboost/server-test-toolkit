package org.test.toolkit.services.memcached;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import net.spy.memcached.AddrUtil;
import net.spy.memcached.MemcachedClient;

import org.test.toolkit.util.CollectionUtil;
import org.test.toolkit.util.ValidationUtil;

public class MemcachedAccessImpl implements MemcachedAccess {

	private final MemcachedClient memcachedClient;

	public MemcachedAccessImpl(InetSocketAddress atLeastOneInetSocketAddress,
			InetSocketAddress... otherInetSocketAddresses) throws IOException {
		ValidationUtil.checkNull(atLeastOneInetSocketAddress);
		ValidationUtil.checkNull(otherInetSocketAddresses);

		List<InetSocketAddress> list = CollectionUtil.getList(atLeastOneInetSocketAddress,
				otherInetSocketAddresses);
		memcachedClient = new MemcachedClient(list);
	}

	/**
	 * @param inetSocketAddressString
	 *            : "host:port host2:port2"
	 * @throws IOException
	 */
	public MemcachedAccessImpl(String inetSocketAddressString) throws IOException {
		ValidationUtil.checkString(inetSocketAddressString);
		memcachedClient = new MemcachedClient(AddrUtil.getAddresses(inetSocketAddressString));
	}

	@Override
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	@Override
	public void set(String key, Object value, int cacheTime, long timeout, TimeUnit timeUnit) {
		Future<Boolean> future = memcachedClient.set(key, cacheTime, value);
		try {
			future.get(timeout, timeUnit);
		} catch (TimeoutException e) {
			future.cancel(true);
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	@Override
	public Object get(String key) throws Exception {
		return memcachedClient.get(key);
	}

	@Override
	public Object asyncGet(String key, long timeout, TimeUnit timeUnit) throws Exception {
		Future<Object> f = memcachedClient.asyncGet(key);
		try {
			Object value = f.get(timeout, timeUnit);
			return value;
		} catch (TimeoutException e) {
			f.cancel(true);
			throw e;
		}
	}

	@Override
	public Map<String, Object> getBulk(Collection<String> keys) throws Exception {
		return memcachedClient.getBulk(keys);
	}

	@Override
	public Map<String, Object> asyncGetBulk(Collection<String> keys, long timeout, TimeUnit timeUnit)
			throws Exception {
		Map<String, Object> returnMap = new HashMap<String, Object>();
		Map<String, Object> myObj = null;
		Future<Map<String, Object>> f = memcachedClient.asyncGetBulk(keys);
		try {
			myObj = f.get(timeout, timeUnit);
			for (String key : keys) {
				returnMap.put(key, myObj.get(key));
			}
			return returnMap;
		} catch (TimeoutException e) {
			f.cancel(true);
			throw e;
		} catch (InterruptedException e) {
			throw e;
		} catch (ExecutionException e) {
			throw e;
		}

	}

	@Override
	public List<String> deleteBulk(Collection<String> keys, long timeout, TimeUnit timeUnit) {
		List<String> failKeys = new ArrayList<String>();
		for (String key : keys) {
			if (!this.delete(key, timeout, timeUnit)) {
				failKeys.add(key);
			}
		}
		return failKeys;
	}

	@Override
	public boolean delete(String key, long timeout, TimeUnit timeUnit) {
		Future<Boolean> future = memcachedClient.delete(key);
		try {
			future.get(timeout, timeUnit);
			return true;
		} catch (TimeoutException e) {
			future.cancel(true);
		} catch (InterruptedException e) {
		} catch (ExecutionException e) {
		}
		return false;
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
