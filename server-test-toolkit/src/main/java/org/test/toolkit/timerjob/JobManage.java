package org.test.toolkit.timerjob;

import java.util.List;
import java.util.Map;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

public interface JobManage {

	Map<JobDetail, List<Trigger>> getJobs(Scheduler scheduler);

}
