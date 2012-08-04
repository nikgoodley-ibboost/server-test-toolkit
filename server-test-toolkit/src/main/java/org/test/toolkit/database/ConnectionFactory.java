package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbConfigException;
import org.test.toolkit.database.exception.DbConnectionException;
import org.test.toolkit.util.StringUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class);

	private static final ComboPooledDataSource DEFAULT_COMBOPOOLEDDATASOURCE = new ComboPooledDataSource();
	private static volatile ConcurrentHashMap<String, ComboPooledDataSource> dbConfigDataSourceMap = new ConcurrentHashMap<String, ComboPooledDataSource>();
	private static volatile ConcurrentHashMap<String, ComboPooledDataSource> configNameDataSourceMap = new ConcurrentHashMap<String, ComboPooledDataSource>();

	static {
		if (DEFAULT_COMBOPOOLEDDATASOURCE.getJdbcUrl() != null)
			dbConfigDataSourceMap.put(getKey(DEFAULT_COMBOPOOLEDDATASOURCE),
					DEFAULT_COMBOPOOLEDDATASOURCE);
	}

	private ConnectionFactory() {
	}

	public static Connection getConnection(DbConfig dbConfig) {
		try {
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(dbConfig);
			return getConnection(comboPooledDataSource);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getComboPooledDataSource(DbConfig dbConfig) {
		String key = dbConfig.getSynchronizedKey();
		if (dbConfigDataSourceMap.containsKey(key))
			return dbConfigDataSourceMap.get(key);

		synchronized (key) {
			if (dbConfigDataSourceMap.containsKey(key))
				return dbConfigDataSourceMap.get(key);

			ComboPooledDataSource comboPoolDataSource = dbConfig.getComboPooledDataSource();
			dbConfigDataSourceMap.put(key, comboPoolDataSource);
			return comboPoolDataSource;
		}
	}

	public static Connection getConnection(ComboPooledDataSource comboPooledDataSource)
			throws SQLException {
		synchronized (getSynchronizedKey(comboPooledDataSource)) {
			return comboPooledDataSource.getConnection();
		}
	}

	private static String getSynchronizedKey(ComboPooledDataSource comboPooledDataSource) {
		return getKey(comboPooledDataSource).intern();
	}

	private static String getKey(ComboPooledDataSource comboPooledDataSource) {
		return StringUtil.concat(comboPooledDataSource.getJdbcUrl()
				+ comboPooledDataSource.getUser());
	}

	public static Connection getConnection() {
		LOGGER.info("use default config for db connection");
		judgeIfConfigLegal(DEFAULT_COMBOPOOLEDDATASOURCE);
		try {
			return getConnection(DEFAULT_COMBOPOOLEDDATASOURCE);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static void judgeIfConfigLegal(ComboPooledDataSource comboPooledDataSource) {
		if (comboPooledDataSource.getDriverClass() == null)
			throw new DbConfigException("no config in classPath or driverClass not configured");
	}

	public static Connection getConnection(String configName) {
		return getConnection(configName, null);
	}

	public static Connection getConnection(String configName, String configPath) {
		try {
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(configName,
					configPath);
			return getConnection(comboPooledDataSource);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getComboPooledDataSource(String configName,
			String configPath) {
		if (configPath == null)
			configPath = "";
		String key = StringUtil.concat(configName, configPath);

		if (configNameDataSourceMap.containsKey(key))
			return configNameDataSourceMap.get(key);

		synchronized (key.intern()) {
			if (configNameDataSourceMap.containsKey(key))
				return configNameDataSourceMap.get(key);

			ComboPooledDataSource comboPooledDataSource = getComboPoolDataSource(configName,
					configPath);
			setComboPooledDataSourceToMap(key, comboPooledDataSource);

			return comboPooledDataSource;
		}
	}

	private static void setComboPooledDataSourceToMap(String key,
			ComboPooledDataSource comboPooledDataSource) {
		configNameDataSourceMap.put(key, comboPooledDataSource);
 		dbConfigDataSourceMap.putIfAbsent(getKey(comboPooledDataSource), comboPooledDataSource);
 	}

	private static ComboPooledDataSource getComboPoolDataSource(String configName, String configPath) {
		ComboPooledDataSource comboPooledDataSource = null;
		if (configPath.isEmpty())
			comboPooledDataSource = new ComboPooledDataSource(configName);
		else {
			System.setProperty("com.mchange.v2.c3p0.cfg.xml", configPath);
			comboPooledDataSource = new ComboPooledDataSource(configName);
		}
		return comboPooledDataSource;
	}

}
