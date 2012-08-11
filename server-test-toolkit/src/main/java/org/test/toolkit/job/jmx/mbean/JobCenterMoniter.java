package org.test.toolkit.job.jmx.mbean;

import org.test.toolkit.job.schedule.Schedulable;

public class JobCenterMoniter implements JobCenterMoniterMBean {

	private Schedulable schedule;

	public JobCenterMoniter(Schedulable scheduleImpl) {
		super();
		this.schedule = scheduleImpl;
	}

	@Override
	public void start() {
		schedule.start();
	}

	@Override
	public void stop() {
		schedule.stop();
	}

	@Override
	public String list() {
		return schedule.list();

	}

}
