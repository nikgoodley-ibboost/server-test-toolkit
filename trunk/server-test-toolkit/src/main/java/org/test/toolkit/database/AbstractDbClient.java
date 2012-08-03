package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public abstract class AbstractDbClient extends JdbcClosableImpl implements JdbcExecutable {

	protected Connection connection;

	protected AbstractDbClient(Connection connection) {
		super();
		this.connection = connection;
	}

	public ResultSetMetaData getMetaData(ResultSet resultSet) {
		try {
			return resultSet.getMetaData();
		} catch (SQLException e) {
		}

		return null;
	}

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
			closeResultSet(resultSet);
		}

		return arrayList;
	}

	public long getTotalCount(String tableName) throws SQLException {
		String queryCountSql = String.format("select count(*) from %s", tableName);
		ResultSet resultSet = query(queryCountSql);
		resultSet.next();
		int count = resultSet.getInt(1);
		return Long.valueOf(count);
	}

	public void close() {
		closeConnection(connection);
	}

}