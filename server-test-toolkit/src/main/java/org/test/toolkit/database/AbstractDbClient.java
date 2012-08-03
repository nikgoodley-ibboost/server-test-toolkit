package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public abstract class AbstractDbClient extends JdbcClosableImpl implements JdbcExecutable {

	private static final Logger LOGGER = Logger.getLogger(AbstractDbClient.class);

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

	public List<HashMap<String, ?>> toRecordMapList(ResultSet resultSet) throws SQLException {
		List<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();
		List<String> columnNames = getAllColumns(resultSet);
		while (resultSet.next()) {
			HashMap<String, Object> hashMap = new HashMap<String, Object>();
			for (String columnName : columnNames) {
				Object value = resultSet.getObject(columnName);
				hashMap.put(columnName, value);
			}
			list.add(hashMap);
		}

		return list;
	}

	public <T> List<T> getValues(ResultSet resultSet, String columnName) {
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

	public List<String> getAllColumns(ResultSet resultSet) {
		ArrayList<String> arrayList = new ArrayList<String>();
		try {
			ResultSetMetaData metaData = getMetaData(resultSet);
			int columnsCount = metaData.getColumnCount();
			LOGGER.info("[DB] Columns count: " + columnsCount);
			for (int i = 1; i < columnsCount; i++) {
				String columnName = metaData.getColumnName(i);
				arrayList.add(columnName);
			}
		} catch (SQLException e) {
			LOGGER.error(e.getMessage(), e);
		}

		LOGGER.info("[DB] Columns: " + arrayList);
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