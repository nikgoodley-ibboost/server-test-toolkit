package org.test.toolkit.database.resultset.handle;

import java.sql.ResultSet;

public interface ResultSetHandle<T> {

	T handle(ResultSet resultSet);

}
