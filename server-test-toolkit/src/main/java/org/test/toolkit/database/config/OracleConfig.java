package org.test.toolkit.database.config;

public class OracleConfig extends ExplicitDbConfig {

	/**
	 * {@value}
	 */
	private static final String JDBC_DRIVER_FOR_ORACLE = "oracle.jdbc.driver.OracleDriver";
	private static final String JDBC_DRIVER_FOR_INET = "com.inet.ora.OraDriver";

	public static OracleConfig getInstanceForOracle(String driverClass, String url, String username,
			String password) {
		return new OracleConfig(JDBC_DRIVER_FOR_ORACLE, url, username, password);
	}

	public static OracleConfig getInstanceForINET(String driverClass, String url, String username,
			String password) {
		return new OracleConfig(JDBC_DRIVER_FOR_INET, url, username, password);
	}

	private OracleConfig(String driverClass, String url, String username, String password) {
		super(driverClass, url, username, password);
	}

}
