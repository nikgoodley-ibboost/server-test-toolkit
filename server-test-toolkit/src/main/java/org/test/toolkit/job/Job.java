package org.test.toolkit.job;

import org.test.toolkit.job.jmx.JmxMonitorable;
import org.test.toolkit.job.schedule.Schedulable;

public interface Job extends JmxMonitorable, Schedulable{

}
