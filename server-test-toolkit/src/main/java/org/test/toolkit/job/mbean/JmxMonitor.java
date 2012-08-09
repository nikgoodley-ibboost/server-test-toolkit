package org.test.toolkit.job.mbean;

import javax.management.ObjectName;

public interface JmxMonitor {
	
	void registerMBean(Object object, ObjectName name);
}
