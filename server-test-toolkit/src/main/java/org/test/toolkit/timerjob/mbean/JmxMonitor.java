package org.test.toolkit.timerjob.mbean;

import javax.management.ObjectName;

public interface JmxMonitor {
	
	void registerMBean(Object object, ObjectName name);
}
