package org.test.toolkit.database.config;

import java.beans.PropertyVetoException;
import java.util.Properties;

import org.test.toolkit.util.ValidationUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

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

	public ComboPooledDataSource getComboPoolDataSource() throws PropertyVetoException {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(driverClass);
		comboPooledDataSource.setJdbcUrl(url);
		comboPooledDataSource.setUser(username);
		comboPooledDataSource.setPassword(password);

		return comboPooledDataSource;
	}

	public ComboPooledDataSource getComboPoolDataSource(Properties properties)
			throws PropertyVetoException {
		ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
		comboPooledDataSource.setDriverClass(driverClass);
		comboPooledDataSource.setJdbcUrl(url);
		comboPooledDataSource.setUser(username);
		comboPooledDataSource.setPassword(password);
		comboPooledDataSource.setProperties(properties);

		return comboPooledDataSource;
	}

	@Override
	public String toString() {
		return "DbConfig [url=" + url + ", username=" + username + ", password=" + password + "]";
	}

}
