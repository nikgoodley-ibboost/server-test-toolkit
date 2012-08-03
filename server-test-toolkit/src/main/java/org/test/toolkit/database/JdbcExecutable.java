package org.test.toolkit.database;

import java.sql.ResultSet;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public interface JdbcExecutable {

	ResultSet query(String sql);

	boolean execute(String sql);

	int update(String sql);

	void close();

}