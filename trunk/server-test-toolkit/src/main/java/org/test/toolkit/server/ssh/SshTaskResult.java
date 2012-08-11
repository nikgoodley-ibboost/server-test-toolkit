package org.test.toolkit.server.ssh;

import org.test.toolkit.util.ValidationUtil;

public class SshTaskResult<K, V> {

	private K host;
	private V result;

	/**
	 * not check result: due to allow result to be null
	 * @param host
	 * @param result
	 */
	public SshTaskResult(K host, V result) {
  		ValidationUtil.checkNull(host);

		this.host = host;
		this.result = result;
	}

	public K getHost() {
		return host;
	}

	public void setHost(K host) {
		this.host = host;
	}

	public V getResult() {
		return result;
	}

	public void setResult(V result) {
		this.result = result;
	}

}
