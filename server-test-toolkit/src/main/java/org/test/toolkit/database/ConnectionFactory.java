package org.test.toolkit.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import com.mchange.v2.c3p0.ComboPooledDataSource;

import org.test.toolkit.database.config.DbConfig;

public class ConnectionFactory {

	private static volatile Map<DbConfig, ComboPooledDataSource> configDataSourceMap = new HashMap<DbConfig, ComboPooledDataSource>();

	private ConnectionFactory() {
	}

	private static ComboPooledDataSource getComboPooledDataSource(DbConfig dbConfig)
			throws PropertyVetoException {
		if (configDataSourceMap.containsKey(dbConfig))
			return configDataSourceMap.get(dbConfig);

		synchronized (dbConfig) {
			if (configDataSourceMap.containsKey(dbConfig))
				return configDataSourceMap.get(dbConfig);

			ComboPooledDataSource comboPoolDataSource = dbConfig.getComboPoolDataSource();
			configDataSourceMap.put(dbConfig, comboPoolDataSource);
			return comboPoolDataSource;
		}
	}

	public static Connection getConnection(DbConfig dbConfig) {
		Connection con = null;
		try {
			ComboPooledDataSource comboPooledDataSource = getComboPooledDataSource(dbConfig);
			con = comboPooledDataSource.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
		return con;
	}
}
