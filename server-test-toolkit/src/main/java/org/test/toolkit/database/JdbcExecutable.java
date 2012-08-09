package org.test.toolkit.database;

import java.sql.SQLException;

import org.test.toolkit.database.resultsethandle.ResultSetHandle;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public interface JdbcExecutable {

	boolean execute(String sql);

	int update(String sql);

	void close();

	int update(String sql, Object[] params) throws SQLException;

	boolean execute(String sql, Object[] params);

	<T> T query(String sql, Object[] params, ResultSetHandle<T> resultHandle);

	<T> T query(String sql, ResultSetHandle<T> resultHandle);

}