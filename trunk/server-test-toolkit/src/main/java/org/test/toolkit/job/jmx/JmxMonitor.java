package org.test.toolkit.job.jmx;

import javax.management.ObjectName;

public interface JmxMonitor {
	
	void registerMBean(Object object, ObjectName name);
}
