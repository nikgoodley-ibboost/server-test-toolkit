package org.test.toolkit.database;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class ComboPooledDataSourceFactory {

	private ComboPooledDataSourceFactory() {
 	}

	static ComboPooledDataSource getDefaultComboPooledDataSource() {
		return new ComboPooledDataSource();
	}

	static ComboPooledDataSource getComboPooledDataSource(String configName) {
		return new ComboPooledDataSource(configName);
	}

}
