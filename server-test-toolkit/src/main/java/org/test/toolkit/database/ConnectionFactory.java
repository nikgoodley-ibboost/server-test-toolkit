package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbConfigException;
import org.test.toolkit.database.exception.DbConnectionException;
import org.test.toolkit.multithread.AbstractCacheAccess;
import org.test.toolkit.util.StringUtil;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private static final Logger LOGGER = Logger.getLogger(ConnectionFactory.class);

	private static volatile ComboPooledDataSource defaultComboPooledDataSource;
	private static volatile ConcurrentHashMap<String, ComboPooledDataSource> dbConfigDataSourceMap = new ConcurrentHashMap<String, ComboPooledDataSource>();
	private static volatile ConcurrentHashMap<String, ComboPooledDataSource> configNameDataSourceMap = new ConcurrentHashMap<String, ComboPooledDataSource>();

	private ConnectionFactory() {
	}

	private static String getKeyForDbConfigDataSourceMap(ComboPooledDataSource comboPooledDataSource) {
		return StringUtil.concat(comboPooledDataSource.getJdbcUrl()
				+ comboPooledDataSource.getUser());
	}

	public static Connection getConnection(DbConfig dbConfig) {
		try {
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(dbConfig, null);
			return getConnection(comboPooledDataSource);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getComboPooledDataSource(final DbConfig dbConfig,
			final ComboPooledDataSource comboPooledDataSource) {
		AbstractCacheAccess<String, ComboPooledDataSource> abstractCacheAccess = new AbstractCacheAccess<String, ComboPooledDataSource>(
				dbConfigDataSourceMap) {
			@Override
			protected ComboPooledDataSource createValue() {
				if (comboPooledDataSource != null)
					return comboPooledDataSource;
				return dbConfig.getComboPooledDataSource();
			}
		};

		return abstractCacheAccess.getObject(getKeyForDbConfigDataSourceMap(dbConfig),
				configNameDataSourceMap);
	}

	private static String getKeyForDbConfigDataSourceMap(DbConfig dbConfig) {
		return dbConfig.getIdenticalKey();
	}

	public static Connection getConnection(ComboPooledDataSource comboPooledDataSource)
			throws SQLException {
		synchronized (getConnectionSynchronizedKey(comboPooledDataSource)) {
			return comboPooledDataSource.getConnection();
		}
	}

	private static String getConnectionSynchronizedKey(ComboPooledDataSource comboPooledDataSource) {
		return StringUtil.concat(getKeyForDbConfigDataSourceMap(comboPooledDataSource),
				"connection").intern();
	}

	public static Connection getConnection() {
		try {
			LOGGER.info("use default c3p0 config");
			ComboPooledDataSource defaultComboPoolDataSource = getDefaultComboPoolDataSource();
			return getConnection(defaultComboPoolDataSource);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getDefaultComboPoolDataSource() {
		if (defaultComboPooledDataSource != null)
			return defaultComboPooledDataSource;

		synchronized (ConnectionFactory.class) {
			if (defaultComboPooledDataSource != null)
				return defaultComboPooledDataSource;

			ComboPooledDataSource comboPooledDataSource = ComboPooledDataSourceFactory
					.getDefaultComboPooledDataSource();
			judgeIfConfigLegal(comboPooledDataSource);
			defaultComboPooledDataSource = setToDbConfigComboPooledDataSourceMap(comboPooledDataSource);

			return defaultComboPooledDataSource;
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

	private static ComboPooledDataSource getComboPooledDataSource(final String configName,
			String configPath) {
		if (configPath == null)
			configPath = "";
		final String tmpConfigPath = configPath;
		AbstractCacheAccess<String, ComboPooledDataSource> abstractCacheAccess = new AbstractCacheAccess<String, ComboPooledDataSource>(
				dbConfigDataSourceMap) {

			@Override
			protected ComboPooledDataSource createValue() {
				ComboPooledDataSource comboPooledDataSource = getComboPoolDataSource(configName,
						tmpConfigPath);
				return setToDbConfigComboPooledDataSourceMap(comboPooledDataSource);
			}
		};

		return abstractCacheAccess.getObject(
				getKeyForConfigNameDataSourceMap(configName, configPath), configNameDataSourceMap);

	}

	private static String getKeyForConfigNameDataSourceMap(String configName, String configPath) {
		return StringUtil.concat(configName, configPath);
	}

	private static ComboPooledDataSource getComboPoolDataSource(String configName, String configPath) {
		ComboPooledDataSource comboPooledDataSource = null;
		if (configPath.isEmpty())
			comboPooledDataSource = ComboPooledDataSourceFactory
					.getComboPooledDataSource(configName);
		else {
			System.setProperty("com.mchange.v2.c3p0.cfg.xml", configPath);
			comboPooledDataSource = ComboPooledDataSourceFactory
					.getComboPooledDataSource(configName);
		}
		return comboPooledDataSource;
	}

	private static ComboPooledDataSource setToDbConfigComboPooledDataSourceMap(
			ComboPooledDataSource comboPooledDataSource) {
		return getComboPooledDataSource(null, comboPooledDataSource);
	}

}
