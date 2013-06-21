package org.test.toolkit.unittest.util;

import junit.framework.Assert;

import org.junit.Test;
import org.test.toolkit.util.StringUtil;

public class StringUtilTest {

	@Test
	public void testConcatWithSpace() {
 		String concatDirectly = StringUtil.concatWithSpace("a", "b", " c");
		Assert.assertEquals(concatDirectly, "a b  c");
	}

	@Test
	public void testConcatDirectly() {
		String concatDirectly = StringUtil.concatDirectly("a", "b", " c");
		Assert.assertEquals(concatDirectly, "ab c");
	}

	@Test
	public void testConcatWithSemicolon() {
		String concatDirectly = StringUtil
				.concatWithSemicolon("  a", "b", " c");
		Assert.assertEquals(concatDirectly, "  a;b; c");
	}
}
