package org.test.toolkit.database.config;

public class DefaultDbConfig implements DbConfig {

	/**
	 * use default c3po xml or properties config
	 */
	public DefaultDbConfig() {
	}

	@Override
	public String getConfigType() {
 		return "default c3po properties or xml config";
	}

}
