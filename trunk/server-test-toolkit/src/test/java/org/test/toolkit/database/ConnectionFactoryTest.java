package org.test.toolkit.database;

import static org.junit.Assert.*;

import org.junit.Test;

public class ConnectionFactoryTest {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		try {
			ConnectionFactory.getConnection("xwh");
 		} catch (Exception e) {
			// TODO: handle exception
		}
		ConnectionFactory.getConnection("xwh");

	}

}
