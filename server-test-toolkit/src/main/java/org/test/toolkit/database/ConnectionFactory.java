package org.test.toolkit.database;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.SQLException;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class ConnectionFactory {

	private ConnectionFactory() {
	}

	private static ComboPooledDataSource ds = null;

	static {
		try {
			
/* 			ds = new ComboPooledDataSource();
 			ds.setDriverClass("oracle.jdbc.driver.OracleDriver");  
 			ds.setJdbcUrl("jdbc:oracle:thin:@10.224.38.21:1585:hfw2dmss2");
 			ds.setUser("DMS");
 			ds.setPassword("pass");
 			ds.setMaxPoolSize(30);
 			ds.setMinPoolSize(10);*/
			
/*			jdbc.driverClassName=com.mysql.jdbc.Driver
					jdbc.url=jdbc:mysql://gsbqa53-11:3306/replication?useUnicode=true&characterEncoding=UTF-8
					jdbc.username=test
					jdbc.password=pass*/
			
			ds = new ComboPooledDataSource();
 			ds.setDriverClass("com.mysql.jdbc.Driver");  
 			ds.setJdbcUrl("jdbc:mysql://10.224.36.143:3306/replication?useUnicode=true&characterEncoding=UTF-8");
 			ds.setUser("test");
 			ds.setPassword("pass");

		} catch (PropertyVetoException e) {
			e.printStackTrace();
		}
	}

	public static synchronized Connection getConnection() {
		Connection con = null;
		try {
			con = ds.getConnection();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		return con;
	}
 }
