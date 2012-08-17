package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.test.toolkit.database.DbUtil.DbCloseUtil;

/**
 * @author fu.jian
 * date Aug 3, 2012
 */
public class JdbcClosableImpl implements JdbcClosable {

	protected JdbcClosableImpl() {
	}

	@Override
	public void closeConnection(Connection connection) {
		DbCloseUtil.closeConnection(connection);
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