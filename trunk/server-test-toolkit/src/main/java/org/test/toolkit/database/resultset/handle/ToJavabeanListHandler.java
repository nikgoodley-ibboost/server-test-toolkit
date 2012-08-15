package org.test.toolkit.database.resultset.handle;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.test.toolkit.database.exception.DbExecuteException;

public class ToJavabeanListHandler<T> implements ResultSetHandler<List<T>> {

	private static final Logger LOGGER = Logger.getLogger(ToJavabeanListHandler.class);

	private Class<T> javabeanClass;

	public ToJavabeanListHandler(Class<T> javaBeanClass) {
		this.javabeanClass = javaBeanClass;
	}

	@Override
	public List<T> handle(ResultSet resultSet) {
		LOGGER.info("[DB][ToJavabeanListHandle][BEGIN]");
		List<T> list = new ArrayList<T>();
		try {
			while (resultSet.next()) {
				T entity = javabeanClass.newInstance();
				ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
				int columnCount = resultSetMetaData.getColumnCount();
				for (int index = 1; index <= columnCount; index++) {
					String key = resultSetMetaData.getColumnName(index);
					Object value = resultSet.getObject(index);
					System.out.println(value.getClass());
					if (LOGGER.isDebugEnabled()) {
						String msg = String.format("[DB][Column][Key:Value][%s:%s]", key, value);
						LOGGER.debug(msg);
					}

					BeanUtils.copyProperty(entity, key, value);
				}
				list.add(entity);
			}
			LOGGER.info("[DB][ToJavabeanListHandle][END][SUCCESS]");
			return list;
		} catch (Exception e) {
			LOGGER.error("[DB][ToJavabeanListHandle][END][FAIL] " + e.getMessage(), e);
			throw new DbExecuteException(e.getMessage(), e);
		}
	}

}