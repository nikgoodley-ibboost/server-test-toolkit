package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;
import org.test.toolkit.database.exception.DbExecuteException;

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
			throw new DbExecuteException(e.getMessage(), e);
		}
	}

	public List<HashMap<String, ?>> toRecordMapList(ResultSet resultSet) {
		List<HashMap<String, ?>> list = new ArrayList<HashMap<String, ?>>();
		List<String> columnNames = getAllColumns(resultSet);
		try {
			while (resultSet.next()) {
				HashMap<String, Object> hashMap = new HashMap<String, Object>();
				for (String columnName : columnNames) {
					Object value = resultSet.getObject(columnName);
					hashMap.put(columnName, value);
				}
				list.add(hashMap);
			}
			return list;
		} catch (SQLException e) {
			throw new DbExecuteException(e.getMessage(), e);
		}
	}

	public <T> List<T> getValues(ResultSet resultSet, String columnName) {
		ArrayList<T> arrayList = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				@SuppressWarnings("unchecked")
				T columnValue = (T) resultSet.getObject(columnName);
				arrayList.add(columnValue);
			}
			return arrayList;
		} catch (SQLException e) {
			throw new DbExecuteException(e.getMessage(), e);
		}
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
			throw new DbExecuteException(e.getMessage(), e);
		}

		LOGGER.info("[DB] Columns: " + arrayList);
		return arrayList;
	}

	public long getTotalCount(String tableName) {
 		String sqlForTableRecordCount = CommonSql.getSqlForTableRecordCount(tableName);
		ResultSet resultSet = query(sqlForTableRecordCount);
		try {
			resultSet.next();
			int count = resultSet.getInt(1);
			return Long.valueOf(count);
		} catch (SQLException e) {
			throw new DbExecuteException(e.getMessage(), e);
		}
 	}

	public void close() {
		closeConnection(connection);
	}

}