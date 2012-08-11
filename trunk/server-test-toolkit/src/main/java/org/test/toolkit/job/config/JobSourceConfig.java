package org.test.toolkit.job.config;

import java.util.Collection;


public interface JobSourceConfig {

	Collection<JobSourceEntry<JobSource>> getJobSourceEntrys();

}
