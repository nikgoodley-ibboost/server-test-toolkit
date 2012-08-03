package org.test.toolkit.database;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * @author fu.jian
 * @date Aug 3, 2012
 */
public interface JdbcClosable {

 	void closeResultSet(ResultSet resultSet);

	void closeStatement(Statement statement);

	void closeConnection(Connection connection);

}