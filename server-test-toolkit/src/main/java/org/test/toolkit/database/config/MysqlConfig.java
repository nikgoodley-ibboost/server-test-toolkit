package org.test.toolkit.database.config;

public class MysqlConfig extends DbConfig {

	private static final String JDBC_DRIVER_FOR_MYSQL = "com.mysql.jdbc.Driver";

	protected MysqlConfig(String url, String username, String password) {
		super(JDBC_DRIVER_FOR_MYSQL, url, username, password);
	}

}
