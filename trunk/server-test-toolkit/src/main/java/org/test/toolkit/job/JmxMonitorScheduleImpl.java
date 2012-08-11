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
import org.test.toolkit.job.config.JobSource;
import org.test.toolkit.job.config.JobSourceConfig;
import org.test.toolkit.job.config.JobSourceConfigImpl;
import org.test.toolkit.job.config.JobSourceEntry;
import org.test.toolkit.job.exception.JobException;
import org.test.toolkit.job.jmx.JmxMonitor;
import org.test.toolkit.job.jmx.mbean.QuartzSchedule;
import org.test.toolkit.util.ValidationUtil;

public class JmxMonitorScheduleImpl implements JmxMonitorSchedulable{

	private final static JobSourceConfig DEFAULT_JOB_MANAGE_CONFIG = new JobSourceConfigImpl();
	private static Scheduler defaultScheduler;

	private static JmxMonitorSchedulable instance;
	private Scheduler scheduler;
	private JobSourceConfig jobManageConfig;

	private JmxMonitorScheduleImpl(Scheduler scheduler, JobSourceConfig jobManageConfig) {
		ValidationUtil.checkNull(scheduler, jobManageConfig);
		this.scheduler = scheduler;
		this.jobManageConfig = jobManageConfig;
 	}

 	public static JmxMonitorSchedulable getInstance() {
		try {
			if (defaultScheduler == null) {
				synchronized (JmxMonitorScheduleImpl.class) {
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
	public void registerMBean(JmxMonitor jmxMonitor) {
		QuartzSchedule quartzSchedule = new QuartzSchedule(this);
		ObjectName name=null;
		 try {
			 name = new ObjectName("com.cisco.jmx:type=quartz");
		} catch (MalformedObjectNameException e) {
 			e.printStackTrace();
		} catch (NullPointerException e) {
 			e.printStackTrace();
		}

		 jmxMonitor.registerMBean(quartzSchedule, name);
	}


	@Override
	public void start(){
 		Collection<JobSourceEntry<JobSource>> jobSourceEntrys = jobManageConfig.getJobSourceEntrys();
		for(JobSourceEntry<JobSource> classEntry: jobSourceEntrys){
			JobSource jobManage = classEntry.getClassInstance();
			Map<JobDetail, List<Trigger>> jobs = jobManage.getJobs(scheduler);

			try {
				scheduler.scheduleJobs(jobs, true);
				scheduler.start();
			} catch (SchedulerException e) {
 				e.printStackTrace();
 				throw new JobException(e);
			}

		}

	}

	public static JmxMonitorSchedulable getInstance(Scheduler scheduler,
			JobSourceConfig jobManageConfig) {
		if (instance == null)
			synchronized (JmxMonitorScheduleImpl.class) {
				if (instance == null)
					instance = new JmxMonitorScheduleImpl(scheduler, jobManageConfig);
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
