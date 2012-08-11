package org.test.toolkit.job;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.management.MalformedObjectNameException;
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
import org.test.toolkit.job.exception.JobException;
import org.test.toolkit.job.jmx.JmxMonitorImpl;
import org.test.toolkit.job.jmx.mbean.JobCenterMoniter;
import org.test.toolkit.util.ValidationUtil;

public class JobCenterImpl implements JobCenter{

	private final static JobConfig DEFAULT_JOB_MANAGE_CONFIG = new JobConfigImpl();
	private static Scheduler defaultScheduler;

	private static JobCenter instance;
	private Scheduler scheduler;
	private JobConfig jobManageConfig;

	private JobCenterImpl(Scheduler scheduler, JobConfig jobManageConfig) {
		ValidationUtil.checkNull(scheduler, jobManageConfig);
		this.scheduler = scheduler;
		this.jobManageConfig = jobManageConfig;
 	}

 	public static JobCenter getInstance() {
		try {
			if (defaultScheduler == null) {
				synchronized (JobCenterImpl.class) {
					if (defaultScheduler == null)
						defaultScheduler = StdSchedulerFactory
								.getDefaultScheduler();
				}
			}
 			return getInstance(defaultScheduler, DEFAULT_JOB_MANAGE_CONFIG);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new JobException(e);
		}
 	}

	@Override
	public void registerMBean() {
		JobCenterMoniter quartzSchedule = new JobCenterMoniter(this);
		ObjectName name=null;
		 try {
			 name = new ObjectName("com.cisco.jmx:type=quartz");
		} catch (MalformedObjectNameException e) {
 			e.printStackTrace();
		} catch (NullPointerException e) {
 			e.printStackTrace();
		}

		 JmxMonitorImpl.getInstance().registerMBean(quartzSchedule, name);
	}


	@Override
	public void start(){
 		Collection<JobEntry<Job>> jobSourceEntrys = jobManageConfig.getJobSourceEntrys();
		for(JobEntry<Job> classEntry: jobSourceEntrys){
			Job jobManage = classEntry.getClassInstance();
			Map<JobDetail, List<Trigger>> jobs = jobManage.getJobDetails(scheduler);

			try {
				scheduler.scheduleJobs(jobs, true);
				scheduler.start();
			} catch (SchedulerException e) {
 				e.printStackTrace();
 				throw new JobException(e);
			}

		}

	}

	public static JobCenter getInstance(Scheduler scheduler,
			JobConfig jobManageConfig) {
		if (instance == null)
			synchronized (JobCenterImpl.class) {
				if (instance == null)
					instance = new JobCenterImpl(scheduler, jobManageConfig);
			}
		return instance;
	}

	@Override
	public void stop() {
 		try {
 		    if(scheduler.isStarted())
			scheduler.clear();
		} catch (SchedulerException e) {
 			e.printStackTrace();
		}
 	}


	@Override
	public String list() {
		try {
			List<JobExecutionContext> currentlyExecutingJobs = scheduler.getCurrentlyExecutingJobs();
		    StringBuffer stringBuffer=new StringBuffer();
		    for(JobExecutionContext jobExecutionContext: currentlyExecutingJobs){
		    	stringBuffer.append(jobExecutionContext.getJobDetail().getKey());
		    	stringBuffer.append("\n");
		    }

		    return stringBuffer.toString();
		} catch (SchedulerException e) {
 			e.printStackTrace();
		}
		return null;

	}
}
