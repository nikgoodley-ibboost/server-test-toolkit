package org.test.toolkit.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbExecuteException;
import org.test.toolkit.database.resultset.handler.ResultSetHandler;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class DefaultDbClient extends AbstractDbClient {

	private static final Logger LOGGER = Logger.getLogger(DefaultDbClient.class);
	/**
	 * {@value}
	 */
	private static final String LOG_FROMAT_FOR_SQL_EXCEPTION = "fail to execute sql : %s for %s";

	/**
	 * use default c3p0.properties
	 * 
	 * @see "c3p0.properties"
	 */
	public static DefaultDbClient getInstanceWithDefaultPropertiesConfig() {
		return new DefaultDbClient();
	}

	public static DefaultDbClient getInstance(DbConfig dbConfig) {
		return new DefaultDbClient(dbConfig);
	}

	/**
	 * if use default c3po-config.xml, can use this method to get instance. one
	 * xml can config serveral db configs, every db config in xml has one
	 * <code>configName</code>.
	 * 
	 * @param configName
	 */
	public static DefaultDbClient getInstanceWithDefaultXmlConfig(String configName) {
		return new DefaultDbClient(configName);
	}

	/**
	 * set c3po-config.xml's path by
	 * <code>configPath<code>,one xml can config serveral db config, every db config in xml has one <code>configName</code>
	 * similar with  {@linkplain DefaultDbClient#getInstanceWithDefaultXmlConfig(String)}.
	 * @param configName
	 * @param configPath
	 */
	public static DefaultDbClient getInstanceWithXmlConfig(String configName, String configPath) {
		return new DefaultDbClient(configName, configPath);
	}

	public static DefaultDbClient getInstance(ComboPooledDataSource comboPooledDataSource) {
		return new DefaultDbClient(comboPooledDataSource);
	}

	private DefaultDbClient() {
		super(ConnectionFactory.getConnection());
	}

	private DefaultDbClient(DbConfig dbConfig) {
		super(ConnectionFactory.getConnection(dbConfig));
	}

	private DefaultDbClient(String configName) {
		super(ConnectionFactory.getConnection(configName));
	}

	private DefaultDbClient(String configName, String configPath) {
		super(ConnectionFactory.getConnection(configName, configPath));
	}

	private DefaultDbClient(ComboPooledDataSource comboPooledDataSource) {
		super(ConnectionFactory.getConnection(comboPooledDataSource));
	}

	@Override
	public <T> T query(String sql, Object[] params, ResultSetHandler<T> resultSetHandler) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}
			resultSet = preparedStatement.executeQuery();

			return resultSetHandler.handle(resultSet);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStatement);
		}
	}

	@Override
	public <T> T query(String sql, ResultSetHandler<T> resultSetHandler) {
		LOGGER.info("execute sql: " + sql);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			return resultSetHandler.handle(resultSet);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeResultSet(resultSet);
			closeStatement(statement);
		}
	}

	@Override
	public boolean execute(String sql, Object[] params) {
		LOGGER.info("execute sql: " + sql);
		PreparedStatement createStatement = null;
		try {
			createStatement = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++)
				createStatement.setObject(i + 1, params[i]);
			return createStatement.execute(sql);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeStatement(createStatement);
		}
	}

	@Override
	public boolean execute(String sql) {
		LOGGER.info("execute sql: " + sql);
		Statement createStatement = null;
		try {
			createStatement = connection.createStatement();
			return createStatement.execute(sql);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeStatement(createStatement);
		}
	}

	@Override
	public int update(String sql) {
		LOGGER.info("execute sql: " + sql);
		Statement createStatement = null;
		try {
			createStatement = connection.createStatement();
			return createStatement.executeUpdate(sql);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeStatement(createStatement);
		}
	}

	@Override
	public int update(String sql, Object[] params) {
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbExecuteException(e.getMessage(), e);
		} finally {
			closeStatement(preparedStatement);
		}
	}

}