package org.test.toolkit.job;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.JobListener;
import org.quartz.Matcher;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.matchers.KeyMatcher;

public final class JobUtil {

	public static final String JOB = "job";
	public static final String _ = "_";

	private JobUtil() {
	}

	static void appendListenerToJob(Scheduler scheduler, JobDetail job,
			JobListener jobListener) {
		Matcher<JobKey> matcher = KeyMatcher.keyEquals(job.getKey());
		try {
			scheduler.getListenerManager().addJobListener(jobListener, matcher);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new RuntimeException("add listener fail:", e);
		}
	}

	static String formatJobName(String jobName) {
		StringBuilder stringBuilder = new StringBuilder(JOB);
		stringBuilder.append(_);
		stringBuilder.append(jobName);

		return stringBuilder.toString();
	}

	static Trigger getDefaultTrigger(String cronTab) {
		ScheduleBuilder<CronTrigger> schedBuilder = CronScheduleBuilder.cronSchedule(cronTab);
		Trigger trigger = TriggerBuilder.newTrigger().withSchedule(schedBuilder).build();

		return trigger;
	}

	static JobDetail getDefaultJobDetail(Class<? extends Job> jobClass, String jobName) {
		JobDetail job = JobBuilder.newJob(jobClass).withIdentity(jobName).build();
		return job;
	}

}
