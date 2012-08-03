package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbConnectionException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private static volatile Map<DbConfig, ComboPooledDataSource> configDataSourceMap = new ConcurrentHashMap<DbConfig, ComboPooledDataSource>();

	private ConnectionFactory() {
	}

	public static Connection getConnection(DbConfig dbConfig) {
		try {
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(dbConfig);
			synchronized (getComboPooledDataSourceSynchronizedKey(comboPooledDataSource)) {
				return comboPooledDataSource.getConnection();
			}
		} catch (SQLException e) {
			throw new DbConnectionException(e.getMessage(), e);
		}
	}

	private static ComboPooledDataSource getComboPooledDataSource(DbConfig dbConfig) {
		if (configDataSourceMap.containsKey(dbConfig))
			return configDataSourceMap.get(dbConfig);

		synchronized (dbConfig.getSynchronizedKey()) {
			if (configDataSourceMap.containsKey(dbConfig))
				return configDataSourceMap.get(dbConfig);

			ComboPooledDataSource comboPoolDataSource = dbConfig.getComboPooledDataSource();
			configDataSourceMap.put(dbConfig, comboPoolDataSource);
			return comboPoolDataSource;
		}
	}

	private static String getComboPooledDataSourceSynchronizedKey(
			ComboPooledDataSource comboPooledDataSource) {
		StringBuilder synchronizedKey = new StringBuilder(comboPooledDataSource.getJdbcUrl());
		synchronizedKey.append(comboPooledDataSource.getUser());

		return synchronizedKey.toString().intern();
	}
}
