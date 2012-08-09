package org.test.toolkit.database.resultsethandle;

import java.sql.ResultSet;

public interface ResultSetHandle<T> {

	T handle(ResultSet resultSet);

}
