package org.test.toolkit.services.memcached;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import net.spy.memcached.MemcachedClient;

public interface MemcachedAccessor {

	MemcachedClient getMemcachedClient();

	void set(String key, Object value, int cacheTime, long timeout, TimeUnit timeUnit);

	Object get(String key) throws Exception;

	Object asyncGet(String key, long timeout, TimeUnit timeUnit) throws Exception;

	Map<String, Object> getBulk(Collection<String> keys) throws Exception;

	Map<String, Object> asyncGetBulk(Collection<String> keys, long timeout, TimeUnit timeUnit)
			throws Exception;

	List<String> deleteBulk(Collection<String> keys, long timeout, TimeUnit timeUnit);

	boolean delete(String key, long timeout, TimeUnit timeUnit);

	void flush();

	void shutdown();

}