package org.test.toolkit.database.config;

import org.test.toolkit.util.ValidationUtil;

public class DbConfig {

	private String driverClass;
	private String url;
	private String username;
	private String password;

	protected DbConfig(String driverClass, String url, String username, String password) {
		super();
		ValidationUtil.effectiveStr(driverClass, url, username, password);

		this.driverClass = driverClass;
		this.url = url;
		this.username = username;
		this.password = password;
	}

	public String getDriverClass() {
		return driverClass;
	}

	public void setDriverClass(String driverClass) {
		this.driverClass = driverClass;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Override
	public String toString() {
		return "DbConfig [url=" + url + ", username=" + username + ", password=" + password + "]";
	}

}
