package org.test.toolkit.timerjob;

import org.test.toolkit.timerjob.mbean.JmxMonitorImpl;

public class QuartzExecute {

	public static void main(String[] args) {

		MonitorSchedule schedule=MonitorScheduleImpl.getInstance();
		schedule.start();
		schedule.registerMBean(JmxMonitorImpl.getInstance());
	}

}
