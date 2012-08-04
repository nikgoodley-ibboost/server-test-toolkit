package org.test.toolkit.database;


public class ConnectionFactoryTest {

	public static void main(String[] args) {
		System.out.println(System.getProperty("java.class.path"));
		try {
			ConnectionFactory.getConnection("test1");
 		} catch (Exception e) {
			// TODO: handle exception
		}
//		ConnectionFactory.getConnection("xwh");

	}

}
