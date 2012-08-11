package org.test.toolkit.server.ssh;

import org.test.toolkit.util.ValidationUtil;

public class OperationResult<K, V> {

	private K key;
	private V value;

	/**
	 * not check value: due to allow value to be null
	 * @param key
	 * @param value
	 */
	public OperationResult(K key, V value) {
  		ValidationUtil.checkNull(key);
		
		this.key = key;
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public V getValue() {
		return value;
	}

	public void setValue(V value) {
		this.value = value;
	}

}
