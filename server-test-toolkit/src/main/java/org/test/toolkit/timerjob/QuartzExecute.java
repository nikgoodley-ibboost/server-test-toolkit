package org.test.toolkit.timerjob;

import org.test.toolkit.timerjob.mbean.JmxMonitorImpl;

public final class QuartzExecute implements Schedulable {

	private MonitorSchedule monitorSchedule;
	private static volatile QuartzExecute instance;

	private QuartzExecute(MonitorSchedule monitorSchedule) {
		this.monitorSchedule = monitorSchedule;
	}

	public static QuartzExecute getInstance() {
		if (instance != null)
			return instance;

		synchronized (QuartzExecute.class) {
			if (instance != null)
				return instance;

			instance = new QuartzExecute(MonitorScheduleImpl.getInstance());
			return instance;
		}
	}

	@Override
	public void start() {
		monitorSchedule.start();
		monitorSchedule.registerMBean(JmxMonitorImpl.getInstance());
	}

	@Override
	public void stop() {
		monitorSchedule.stop();
	}

	@Override
	public String list() {
		return monitorSchedule.list();
	}

	public static void main(String[] args) {
		QuartzExecute.getInstance().start();
	}

}
