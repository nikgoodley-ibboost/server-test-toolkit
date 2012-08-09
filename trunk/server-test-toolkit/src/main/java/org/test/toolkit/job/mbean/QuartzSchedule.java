package org.test.toolkit.job.mbean;

import org.test.toolkit.job.Schedulable;

 

public class QuartzSchedule implements QuartzScheduleMBean {
	
	private Schedulable schedule;
	
 	public QuartzSchedule(Schedulable scheduleImpl) {
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
