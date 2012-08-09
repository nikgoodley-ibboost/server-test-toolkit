package org.test.toolkit.database.resultsethandle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.test.toolkit.database.exception.DbExecuteException;

public class ToJavabeanListHandle<T> implements ResultSetHandle<List<T>> {

	private Class<T> javabeanClass;

	public ToJavabeanListHandle(Class<T> javaBeanClass) {
		this.javabeanClass = javaBeanClass;
	}

	@Override
	public List<T> handle(ResultSet resultSet) {
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				T entity = javabeanClass.newInstance();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int columnCount = resultSetMetaData.getColumnCount();
				for (int index = 0; index < columnCount; index++) {
					String key = resultSetMetaData.getColumnName(index);
					Object value = resultSet.getObject(index);
					BeanUtils.copyProperty(entity, key, value);
				}
				list.add(entity);
			}
			return list;
		} catch (Exception e) {
			throw new DbExecuteException(e.getMessage(), e);
		}
	}

}
