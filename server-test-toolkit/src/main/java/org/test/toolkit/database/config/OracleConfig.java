package org.test.toolkit.database.config;

public class OracleConfig extends DbConfig {

	private static final String JDBC_DRIVER_FOR_ORACLE = "oracle.jdbc.driver.OracleDriver";

	public OracleConfig(String url, String username, String password) {
 		super(JDBC_DRIVER_FOR_ORACLE, url, username, password);
	}

}
