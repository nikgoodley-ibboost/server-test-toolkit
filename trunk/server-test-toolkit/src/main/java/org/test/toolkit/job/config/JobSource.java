package org.test.toolkit.job.config;

import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

public interface JobSource {

	Map<JobDetail, List<Trigger>> getJobs(Scheduler scheduler);

}
