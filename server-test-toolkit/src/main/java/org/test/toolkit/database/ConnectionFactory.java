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

	private static volatile ComboPooledDataSource DEFAULT_COMBOPOOLEDDATASOURCE;
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
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(dbConfig,null);
			return getConnection(comboPooledDataSource);
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getComboPooledDataSource(DbConfig dbConfig, ComboPooledDataSource comboPooledDataSource) {
		String keyForDbConfigDataSourceMap = getKeyForDbConfigDataSourceMap(dbConfig);
		if (dbConfigDataSourceMap.containsKey(keyForDbConfigDataSourceMap))
			return dbConfigDataSourceMap.get(keyForDbConfigDataSourceMap);

		synchronized (configNameDataSourceMap) {
			if (dbConfigDataSourceMap.containsKey(keyForDbConfigDataSourceMap))
				return dbConfigDataSourceMap.get(keyForDbConfigDataSourceMap);

			if(comboPooledDataSource==null)
				comboPooledDataSource = dbConfig.getComboPooledDataSource();
			dbConfigDataSourceMap.put(keyForDbConfigDataSourceMap, comboPooledDataSource);

			return comboPooledDataSource;
		}
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
		if (DEFAULT_COMBOPOOLEDDATASOURCE != null)
			return DEFAULT_COMBOPOOLEDDATASOURCE;

		synchronized (ConnectionFactory.class) {
			if (DEFAULT_COMBOPOOLEDDATASOURCE != null)
				return DEFAULT_COMBOPOOLEDDATASOURCE;

			ComboPooledDataSource comboPooledDataSource = new ComboPooledDataSource();
			judgeIfConfigLegal(comboPooledDataSource);
			DEFAULT_COMBOPOOLEDDATASOURCE = setToDbConfigComboPooledDataSourceMap(comboPooledDataSource);

			return DEFAULT_COMBOPOOLEDDATASOURCE;
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

		String keyForConfigNameDataSourceMap = getKeyForConfigNameDataSourceMap(configName,
				configPath);

		if (configNameDataSourceMap.containsKey(keyForConfigNameDataSourceMap))
			return configNameDataSourceMap.get(keyForConfigNameDataSourceMap);

		synchronized (keyForConfigNameDataSourceMap.intern()) {
			if (configNameDataSourceMap.containsKey(keyForConfigNameDataSourceMap))
				return configNameDataSourceMap.get(keyForConfigNameDataSourceMap);

			ComboPooledDataSource comboPooledDataSource = getComboPoolDataSource(configName,
					configPath);
			return setComboPooledDataSourceToMapAndReturnDataSource(keyForConfigNameDataSourceMap,
					comboPooledDataSource);
		}
	}

	private static String getKeyForConfigNameDataSourceMap(String configName, String configPath) {
		return StringUtil.concat(configName, configPath);
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

	private static ComboPooledDataSource setComboPooledDataSourceToMapAndReturnDataSource(
			String key, ComboPooledDataSource comboPooledDataSource) {
		setToConfigNameDataSourceMap(key, comboPooledDataSource);
		return setToDbConfigComboPooledDataSourceMap(comboPooledDataSource);
	}

	private static void setToConfigNameDataSourceMap(String key,
			ComboPooledDataSource comboPooledDataSource) {
		configNameDataSourceMap.put(key, comboPooledDataSource);
	}

	private static ComboPooledDataSource setToDbConfigComboPooledDataSourceMap(
			ComboPooledDataSource comboPooledDataSource) {
 		return getComboPooledDataSource(null, comboPooledDataSource);
 	}

}
