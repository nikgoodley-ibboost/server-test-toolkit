package org.test.toolkit.database;

import static org.test.toolkit.database.SqlConstants.*;

import org.test.toolkit.util.ValidationUtil;

public class CommonSql {

	private static final String SQL_FORMAT_FOR_TABLE_RECORD_COUNT = SELECT + SPACE + TOTALCOUNT
			+ SPACE + FROM + SPACE + "%s";

	private CommonSql() {
	}

	public static String getSqlForTableRecordCount(String tableName) {
		ValidationUtil.effectiveStr(tableName);
		return String.format(SQL_FORMAT_FOR_TABLE_RECORD_COUNT, tableName);
	}

}
