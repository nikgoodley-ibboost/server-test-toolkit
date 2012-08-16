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

import org.test.toolkit.server.common.exception.ServerConnectionException;
import org.test.toolkit.services.exception.ServiceExecuteException;
import org.test.toolkit.services.exception.ServiceTimeoutException;
import org.test.toolkit.util.CollectionUtil;
import org.test.toolkit.util.ValidationUtil;

public class DefaultMemcachedService extends AbstractMemcachedService {

	private final MemcachedClient memcachedClient;

	private static abstract class AbstractFutureResult<M, N> {

		protected Future<M> future;
		protected long timeout;
		protected TimeUnit timeUnit;

		protected AbstractFutureResult(Future<M> future, long timeout, TimeUnit timeUnit) {
			super();
			this.future = future;
			this.timeout = timeout;
			this.timeUnit = timeUnit;
		}

		protected N getResult() {
			try {
				return execute();
			} catch (TimeoutException e) {
				future.cancel(true);
				throw new ServiceTimeoutException(e.getMessage(), e);
			} catch (Exception e) {
				throw new ServiceExecuteException(e.getMessage(), e);
			}
		}

		protected abstract N execute() throws TimeoutException, InterruptedException, ExecutionException;
	}

	private static class DefaultFutureResult<T> extends AbstractFutureResult<T, T> {

		protected DefaultFutureResult(Future<T> future, long timeout, TimeUnit timeUnit) {
			super(future, timeout, timeUnit);
		}

		@Override
		protected T execute() throws TimeoutException, InterruptedException, ExecutionException {
			return future.get(timeout, timeUnit);
		}

	}

	public static DefaultMemcachedService getInstance(InetSocketAddress atLeastOneInetSocketAddress,
			InetSocketAddress... otherInetSocketAddresses) {
		return new DefaultMemcachedService(atLeastOneInetSocketAddress, otherInetSocketAddresses);
	}

	/**
	 * @param inetSocketAddressString
	 *            :<b>"host:port host2:port2"</b>
	 * @return
	 */
	public static DefaultMemcachedService getInstance(String inetSocketAddressString) {
		return new DefaultMemcachedService(inetSocketAddressString);
	}

	private DefaultMemcachedService(InetSocketAddress atLeastOneInetSocketAddress,
			InetSocketAddress... otherInetSocketAddresses) {
		ValidationUtil.checkNull(atLeastOneInetSocketAddress);
		ValidationUtil.checkNull(otherInetSocketAddresses);

		List<InetSocketAddress> list = CollectionUtil.toList(atLeastOneInetSocketAddress,
				otherInetSocketAddresses);
		try {
			memcachedClient = new MemcachedClient(list);
		} catch (IOException e) {
			throw new ServerConnectionException(e.getMessage(), e);
		}
	}

	/**
	 * @param inetSocketAddressString
	 *            :<b>"host:port host2:port2"</b>
	 * @throws IOException
	 */
	private DefaultMemcachedService(String inetSocketAddressString) {
		ValidationUtil.checkString(inetSocketAddressString);
		try {
			memcachedClient = new MemcachedClient(AddrUtil.getAddresses(inetSocketAddressString));
		} catch (IOException e) {
			throw new ServerConnectionException(e.getMessage(), e);
		}
	}

	@Override
	public MemcachedClient getMemcachedClient() {
		return memcachedClient;
	}

	@Override
	public void set(String key, Object value, int cacheTime, long timeout, TimeUnit timeUnit) {
		Future<Boolean> future = memcachedClient.set(key, cacheTime, value);
		new DefaultFutureResult<Boolean>(future, timeout, timeUnit).getResult();
	}

	@Override
	public Object get(String key) {
		return memcachedClient.get(key);
	}

	@Override
	public Object asyncGet(String key, long timeout, TimeUnit timeUnit) {
		Future<Object> future = memcachedClient.asyncGet(key);
		return new DefaultFutureResult<Object>(future, timeout, timeUnit).getResult();

	}

	@Override
	public Map<String, Object> getBulk(Collection<String> keys) {
		return memcachedClient.getBulk(keys);
	}

	@Override
	public Map<String, Object> asyncGetBulk(final Collection<String> keys, long timeout, TimeUnit timeUnit) {
		Future<Map<String, Object>> future = memcachedClient.asyncGetBulk(keys);

		return new AbstractFutureResult<Map<String, Object>, Map<String, Object>>(future, timeout, timeUnit) {

			@Override
			protected Map<String, Object> execute() throws TimeoutException, InterruptedException,
					ExecutionException {
				Map<String, Object> result = future.get(timeout, timeUnit);
				Map<String, Object> returnMap = new HashMap<String, Object>();
				for (String key : keys) {
					returnMap.put(key, result.get(key));
				}
				return returnMap;
			}
		}.getResult();

	}

	@Override
	public List<String> deleteBulk(Collection<String> keys, long timeout, TimeUnit timeUnit) {
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
		new DefaultFutureResult<Boolean>(future, timeout, timeUnit).getResult();

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
