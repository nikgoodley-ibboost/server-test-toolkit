package org.test.toolkit.job;

import org.test.toolkit.job.jmx.JmxMonitorImpl;
import org.test.toolkit.job.schedule.Schedulable;

public final class JobExecutor implements Schedulable {

	private Job monitorSchedule;
	private static volatile JobExecutor instance;

	private JobExecutor(Job monitorSchedule) {
		this.monitorSchedule = monitorSchedule;
	}

	public static JobExecutor getInstance() {
		if (instance != null)
			return instance;

		synchronized (JobExecutor.class) {
			if (instance != null)
				return instance;

			instance = new JobExecutor(JobImpl.getInstance());
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
		JobExecutor.getInstance().start();
	}

}
