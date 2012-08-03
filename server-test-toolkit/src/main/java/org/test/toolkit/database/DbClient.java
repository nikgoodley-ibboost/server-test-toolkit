package org.test.toolkit.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.test.toolkit.database.config.DbConfig;

public class DbClient extends AbstractDbClient {

	private static final Logger LOGGER = Logger.getLogger(DbClient.class);

	public DbClient(DbConfig dbConfig) {
		super(ConnectionFactory.getConnection(dbConfig));
	}

	@Override
	public ResultSet query(String sql) {
		Statement createStatement;
		try {
			createStatement = connection.createStatement();
			return createStatement.executeQuery(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}
		return null;
	}

	@Override
	public boolean execute(String sql) {
		Statement createStatement = null;
		try {
			createStatement = connection.createStatement();
			return createStatement.execute(sql);

		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			closeStatement(createStatement);
		}

		return false;
	}

	@Override
	public int update(String sql) {
		Statement createStatement = null;
		try {
			createStatement = connection.createStatement();
			return createStatement.executeUpdate(sql);
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			closeStatement(createStatement);
		}

		return 0;
	}

}