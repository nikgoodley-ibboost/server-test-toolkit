package org.test.toolkit.database;

import static org.test.toolkit.database.SqlConstants.FROM;
import static org.test.toolkit.database.SqlConstants.SELECT;
import static org.test.toolkit.database.SqlConstants.TOTALCOUNT;

import org.test.toolkit.util.StringUtil;
import org.test.toolkit.util.ValidationUtil;

public class CommonSql {

 	private final static String SQL_FORMAT_FOR_TABLE_RECORD_COUNT = StringUtil.concatWithSpace(
			SELECT, TOTALCOUNT, FROM, "%s");

	/**
	 * @param tableName
	 * @return use {@link #SQL_FORMAT_FOR_TABLE_RECORD_COUNT} and {@code tableName} to get sql for records count
	 */
	public static String getSqlForTableRecordCount(String tableName) {
		ValidationUtil.checkString(tableName);
		return String.format(SQL_FORMAT_FOR_TABLE_RECORD_COUNT, tableName);

	}

	private CommonSql() {
	}

}
