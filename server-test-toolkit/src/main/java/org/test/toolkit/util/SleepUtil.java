package org.test.toolkit.util;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

public class SleepUtil {

	private final static Logger LOGGER = Logger.getLogger(SleepUtil.class);

	private SleepUtil() {
	}

	public static void sleep(TimeUnit timeUnit, long time) {
		try {
			timeUnit.sleep(time);
		} catch (InterruptedException e) {
			LOGGER.warn("stop waiting for:" + e.getMessage());
		}
	}

}
