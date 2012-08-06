package org.test.toolkit.timerjob;

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
import org.test.toolkit.timerjob.mbean.JmxMonitor;
import org.test.toolkit.timerjob.mbean.QuartzSchedule;
import org.test.toolkit.util.ValidationUtil;

public class MonitorScheduleImpl implements MonitorSchedule{

	private final static JobManageConfig DEFAULT_JOB_MANAGE_CONFIG = new JobManageConfigImpl();
	private static Scheduler defaultScheduler;

	private static MonitorSchedule instance;
	private Scheduler scheduler;
	private JobManageConfig jobManageConfig;

	private MonitorScheduleImpl(Scheduler scheduler, JobManageConfig jobManageConfig) {
		ValidationUtil.nonNull(scheduler, jobManageConfig);
		this.scheduler = scheduler;
		this.jobManageConfig = jobManageConfig;
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

	public static MonitorSchedule getInstance() {
		try {
			if (defaultScheduler == null) {
				synchronized (MonitorScheduleImpl.class) {
					if (defaultScheduler == null)
						defaultScheduler = StdSchedulerFactory
								.getDefaultScheduler();
				}
			}
 			return getInstance(defaultScheduler, DEFAULT_JOB_MANAGE_CONFIG);
		} catch (SchedulerException e) {
			e.printStackTrace();
			throw new JobManageException(e);
		}

	}

	@Override
	public void start(){

		Collection<ClassEntry<JobManage>> jobManageEntrys = jobManageConfig.readJobManageEntrys();
		for(ClassEntry<JobManage> classEntry: jobManageEntrys){
			JobManage jobManage = classEntry.getClassInstance();
			Map<JobDetail, List<Trigger>> jobs = jobManage.getJobs(scheduler);

			try {
				scheduler.scheduleJobs(jobs, true);
				scheduler.start();
			} catch (SchedulerException e) {
 				e.printStackTrace();
 				throw new JobManageException(e);
			}

		}

	}

	public static MonitorSchedule getInstance(Scheduler scheduler,
			JobManageConfig jobManageConfig) {
		if (instance == null)
			synchronized (MonitorScheduleImpl.class) {
				if (instance == null)
					instance = new MonitorScheduleImpl(scheduler, jobManageConfig);
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
