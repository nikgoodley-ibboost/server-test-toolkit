package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.test.toolkit.database.DbUtil.DbCloseUtil;
import org.test.toolkit.database.config.DbConfig;

public class DbClientImpl implements DbClient {

	static final Logger LOGGER = Logger.getLogger(DbClientImpl.class);

	private Connection connection;

	public DbClientImpl(DbConfig dbConfig) {
		this.connection = ConnectionFactory.getConnection(dbConfig);
	}

	@Override
	public ResultSet query(String sql) {
		Statement createStatement;
		try {
			createStatement = connection.createStatement();
			return createStatement.executeQuery(sql);
		} catch (SQLException e) {
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
		} finally {
			DbCloseUtil.closeStatement(createStatement);
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
		} finally {
			DbCloseUtil.closeStatement(createStatement);
		}

		return 0;
	}

	@Override
	public ResultSetMetaData getMetaData(ResultSet resultSet) {
		try {
			return resultSet.getMetaData();
		} catch (SQLException e) {
		}

		return null;
	}
	
 
	@Override
	public <T> List<T> getColumnValues(ResultSet resultSet, String columnName) {
		ArrayList<T> arrayList = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				@SuppressWarnings("unchecked")
				T columnValue = (T) resultSet.getObject(columnName);
				arrayList.add(columnValue);
			}
		} catch (SQLException e) {
		}

		return arrayList;
	}

	@Override
	public List<String> getColumnNames(ResultSet resultSet) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			ResultSetMetaData metaData = getMetaData(resultSet);
			int max_column_number = metaData.getColumnCount();
			for (int i = 0; i < max_column_number; i++) {
				arrayList.add(metaData.getColumnName(i));
			}
		} catch (SQLException e) {

		} finally {
			DbCloseUtil.closeResultSet(resultSet);
		}

		return arrayList;
	}

	@Override
	public long getTotalCount(String tableName) throws SQLException {
		String queryCountSql = String.format("select count(*) from %s", tableName);
		ResultSet resultSet = query(queryCountSql);

		resultSet.next();
		int count = resultSet.getInt(1);
		return Long.valueOf(count);
	}

	@Override
	public void closeConnection() {
		DbCloseUtil.closeConnection(this.connection);
	}

	@Override
	public void closeResultSet(ResultSet resultSet) {
		DbCloseUtil.closeResultSet(resultSet);
	}

	@Override
	public void closeStatement(Statement statement) {
		DbCloseUtil.closeStatement(statement);
	}

}