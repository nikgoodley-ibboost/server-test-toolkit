package org.test.toolkit.database.resultset.handle;

import java.sql.ResultSet;

public interface ResultSetHandler<T> {

	T handle(ResultSet resultSet);

}
