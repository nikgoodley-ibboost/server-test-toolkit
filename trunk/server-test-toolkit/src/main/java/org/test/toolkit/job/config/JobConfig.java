package org.test.toolkit.job.config;

import java.util.Collection;


public interface JobConfig {

	Collection<JobEntry<Job>> getJobSourceEntrys();

}
