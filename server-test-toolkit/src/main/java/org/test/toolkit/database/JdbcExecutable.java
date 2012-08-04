package org.test.toolkit.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public interface JdbcExecutable {

	ResultSet query(String sql);

	boolean execute(String sql);

	int update(String sql);

	void close();

	int update(String sql, Object[] params) throws SQLException;

	ResultSet query(String sql, Object[] params) throws SQLException;

	boolean execute(String sql, Object[] params);

}