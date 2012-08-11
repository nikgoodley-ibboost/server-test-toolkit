package org.test.toolkit.database;

import java.sql.Connection;
import java.util.HashMap;
import java.util.List;

import org.test.toolkit.database.resultset.handle.ToMapListHandle;
import org.test.toolkit.util.ValidationUtil;

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

	public long getTotalCount(String tableName) {
		ValidationUtil.checkNull(tableName);

		String sqlForTableRecordCount = CommonSql.getSqlForTableRecordCount(tableName);
		List<HashMap<String, ?>> resultList = query(sqlForTableRecordCount, new ToMapListHandle());
		HashMap<String, ?> hashMap = resultList.get(0);
		Object object = hashMap.get(SqlConstants.TOTALCOUNT);

		return Long.valueOf(object.toString());
	}

	public void close() {
		closeConnection(connection);
	}

}