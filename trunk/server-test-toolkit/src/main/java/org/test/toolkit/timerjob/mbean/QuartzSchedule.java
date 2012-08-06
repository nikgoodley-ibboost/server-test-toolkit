package org.test.toolkit.timerjob.mbean;

import org.test.toolkit.timerjob.Schedulable;

 

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
