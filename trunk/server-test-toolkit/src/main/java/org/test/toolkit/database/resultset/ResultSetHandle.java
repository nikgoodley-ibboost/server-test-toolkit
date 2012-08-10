package org.test.toolkit.database.resultset;

import java.sql.ResultSet;

public interface ResultSetHandle<T> {

	T handle(ResultSet resultSet);

}
