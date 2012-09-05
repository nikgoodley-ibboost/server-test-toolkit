package org.test.toolkit.database;

import static org.junit.Assert.*;

import org.junit.Test;
import org.test.toolkit.database.config.XmlDbConfig;

public class DefaultDbClientTest {

	@Test
	public void testDefaultDbClient() {
 		DefaultDbClient defaultDbClient = new DefaultDbClient();
		defaultDbClient.getConnnection();
 	}

	@Test
	public void testDefaultDbClient2() {
 		DefaultDbClient defaultDbClient = new DefaultDbClient(new XmlDbConfig(
				"test"));
		defaultDbClient.getConnnection();
 	}

	@Test
	public void testDefaultDbClient3() {
 		DefaultDbClient defaultDbClient = new DefaultDbClient(new XmlDbConfig(
				"default"));
		defaultDbClient.getConnnection();
 	}

	@Test
	public void testDefaultDbClient4() {
 		DefaultDbClient defaultDbClient = new DefaultDbClient(new XmlDbConfig());
		defaultDbClient.getConnnection();
 	}

	@Test
	public void testDefaultDbClientDbConfig() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryStringObjectArrayResultSetHandlerOfT() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetConnnection() {
		fail("Not yet implemented");
	}

	@Test
	public void testQueryStringResultSetHandlerOfT() {
		fail("Not yet implemented");
	}

	@Test
	public void testExecuteStringObjectArray() {
		fail("Not yet implemented");
	}

	@Test
	public void testExecuteString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateString() {
		fail("Not yet implemented");
	}

	@Test
	public void testUpdateStringObjectArray() {
		fail("Not yet implemented");
	}

}
