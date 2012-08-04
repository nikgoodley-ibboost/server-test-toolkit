package org.test.toolkit.database;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;
import org.test.toolkit.database.exception.DbExecuteException;

public class DbClient extends AbstractDbClient {

	private static final Logger LOGGER = Logger.getLogger(DbClient.class);
	private static final String LOG_FROMAT_FOR_SQL_EXCEPTION = "fail to execute sql : %s for %s";

	public DbClient(DbConfig dbConfig) {
		super(ConnectionFactory.getConnection(dbConfig));
	}

	@Override
	public ResultSet query(String sql, Object[] params) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		try {
			pstmt = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			rs = pstmt.executeQuery();
			return rs;
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
		}
	}

	@Override
	public ResultSet query(String sql) {
		LOGGER.info("execute sql: " + sql);
		Statement createStatement;
		try {
			createStatement = connection.createStatement();
			return createStatement.executeQuery(sql);
		} catch (SQLException e) {
			String msg = String.format(LOG_FROMAT_FOR_SQL_EXCEPTION, sql, e.getMessage());
			throw new DbExecuteException(msg, e);
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
	public int update(String sql, Object[] params) throws SQLException {
		PreparedStatement pstmt = null;
		try {
			pstmt = connection.prepareStatement(sql);
			for (int i = 0; params != null && i < params.length; i++) {
				pstmt.setObject(i + 1, params[i]);
			}
			return pstmt.executeUpdate();
		} finally {
			closeStatement(pstmt);
		}
	}

}