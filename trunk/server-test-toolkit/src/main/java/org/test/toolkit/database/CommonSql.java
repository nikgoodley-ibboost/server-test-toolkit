package org.test.toolkit.database;

import static org.test.toolkit.database.SqlConstants.*;

import org.test.toolkit.util.StringUtil;
import org.test.toolkit.util.ValidationUtil;

public class CommonSql {

	private static final String SQL_FORMAT_FOR_TABLE_RECORD_COUNT = StringUtil.concatWithSpace(
			SELECT, TOTALCOUNT, FROM, "%s");

	private CommonSql() {
	}

	public static String getSqlForTableRecordCount(String tableName) {
		ValidationUtil.checkString(tableName);
		return String.format(SQL_FORMAT_FOR_TABLE_RECORD_COUNT, tableName);

	}

}
