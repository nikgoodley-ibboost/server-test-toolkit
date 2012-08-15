package org.test.toolkit.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbExecuteException;
import org.test.toolkit.database.resultset.handle.ResultSetHandler;

public class DbClient extends AbstractDbClient {

	private static final Logger LOGGER = Logger.getLogger(DbClient.class);
	private static final String LOG_FROMAT_FOR_SQL_EXCEPTION = "fail to execute sql : %s for %s";

	public DbClient(DbConfig dbConfig) {
		super(ConnectionFactory.getConnection(dbConfig));
	}

	@Override
	public <T> T query(String sql, Object[] params, ResultSetHandler<T> resultHandle) {
		PreparedStatement preparedStatement = null;
		ResultSet resultSet = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}
			resultSet = preparedStatement.executeQuery();

			return resultHandle.handle(resultSet);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		} finally {
			closeResultSet(resultSet);
			closeStatement(preparedStatement);
		}
	}

	@Override
	public <T> T query(String sql, ResultSetHandler<T> resultHandle) {
		LOGGER.info("execute sql: " + sql);
		Statement statement = null;
		ResultSet resultSet = null;
		try {
			statement = connection.createStatement();
			resultSet = statement.executeQuery(sql);

			return resultHandle.handle(resultSet);
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
	public int update(String sql, Object[] params){
		PreparedStatement preparedStatement = null;
		try {
			preparedStatement = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				preparedStatement.setObject(i + 1, params[i]);
			}
			return preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new DbExecuteException(e.getMessage(),e);
 		} finally {
			closeStatement(preparedStatement);
		}
	}

}