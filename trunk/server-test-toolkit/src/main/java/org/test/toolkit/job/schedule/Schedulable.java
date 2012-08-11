package org.test.toolkit.job.schedule;

public interface Schedulable {

	public abstract void start();

	public abstract void stop();

	public abstract String list();

}