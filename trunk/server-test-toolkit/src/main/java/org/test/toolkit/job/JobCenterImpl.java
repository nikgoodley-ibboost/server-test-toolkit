package org.test.toolkit.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.JMException;
import javax.management.ObjectName;

import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.test.toolkit.job.config.Job;
import org.test.toolkit.job.config.JobConfig;
import org.test.toolkit.job.config.JobConfigImpl;
import org.test.toolkit.job.config.JobEntry;
import org.test.toolkit.job.exception.JobConfigException;
import org.test.toolkit.job.exception.JobException;
import org.test.toolkit.job.exception.JobExecuteException;
import org.test.toolkit.job.exception.JobMonitorException;
import org.test.toolkit.job.jmx.JmxMonitorImpl;
import org.test.toolkit.job.jmx.mbean.JobCenterSchedule;
import org.test.toolkit.util.ValidationUtil;

public class JobCenterImpl implements JobCenter {

	private static volatile JobCenter instance;

	private Scheduler scheduler;
	private JobConfig jobConfig;

	public static JobCenter getInstance() {
		try {
			return getInstance(StdSchedulerFactory.getDefaultScheduler(), JobConfigImpl.getInstance());
		} catch (SchedulerException e) {
 			throw new JobConfigException(e);
		}
	}

	public static JobCenter getInstance(String configPath) {
		try {
			return getInstance(StdSchedulerFactory.getDefaultScheduler(), JobConfigImpl.getInstance(configPath));
		} catch (SchedulerException e) {
 			throw new JobConfigException(e);
		}
	}

	public static JobCenter getInstance(Scheduler scheduler, JobConfig jobConfig) {
		if (instance == null)
			synchronized (JobCenterImpl.class) {
				if (instance == null)
					instance = new JobCenterImpl(scheduler, jobConfig);
			}
		return instance;
	}

	private JobCenterImpl(Scheduler scheduler, JobConfig jobConfig) {
		ValidationUtil.checkNull(scheduler, jobConfig);
		this.scheduler = scheduler;
		this.jobConfig = jobConfig;
	}

	@Override
	public void registerMBean() {
		JobCenterSchedule jobCenterSchedule = new JobCenterSchedule(this);
		try {
			ObjectName name = new ObjectName("com.cisco.jmx:type=quartz");
			JmxMonitorImpl.getInstance().registerMBean(jobCenterSchedule, name);
		} catch (JMException e) {
			throw new JobMonitorException(e.getMessage(), e);
 		}

	}

	@Override
	public void start() {
		Collection<JobEntry<Job>> jobEntrys = jobConfig.getJobEntrys();
		for (JobEntry<Job> jobEntry : jobEntrys) {
			Job jobManage = jobEntry.getClassInstance();
			Map<JobDetail, List<Trigger>> jobDetails = jobManage.getJobDetails(scheduler);

			try {
				scheduler.scheduleJobs(jobDetails, true);
				scheduler.start();
			} catch (SchedulerException e) {
 				throw new JobConfigException(e.getMessage(),e);
			}
		}
	}

	@Override
	public void stop() {
		try {
			if (scheduler.isStarted())
				scheduler.clear();
		} catch (SchedulerException e) {
			throw new JobExecuteException(e.getMessage(),e);
		}
	}

	@Override
	public String list() {
		try {
			List<JobExecutionContext> currentlyExecutingJobs = scheduler
					.getCurrentlyExecutingJobs();
			StringBuffer stringBuffer = new StringBuffer();
			for (JobExecutionContext jobExecutionContext : currentlyExecutingJobs) {
				stringBuffer.append(jobExecutionContext.getJobDetail().getKey());
				stringBuffer.append("\n");
			}

			return stringBuffer.toString();
		} catch (SchedulerException e) {
			throw new JobExecuteException(e.getMessage(), e);
		}

	}
}
