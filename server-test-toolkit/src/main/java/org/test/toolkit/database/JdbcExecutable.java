package org.test.toolkit.database;

import org.test.toolkit.database.resultset.handler.ResultSetHandler;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public interface JdbcExecutable {

	boolean execute(String sql);

	int update(String sql);

	void close();

	int update(String sql, Object[] params);

	boolean execute(String sql, Object[] params);

	<T> T query(String sql, Object[] params, ResultSetHandler<T> resultHandle);

	<T> T query(String sql, ResultSetHandler<T> resultHandle);

}