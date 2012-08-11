package org.test.toolkit.job.config;

import org.test.toolkit.job.exception.JobException;

public class JobEntry<T> {

	private String name;
	private String className;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public T getClassInstance() {
		try {
			@SuppressWarnings("unchecked")
			Class<T> clazz = (Class<T>) Class.forName(className);
			return clazz.newInstance();
		} catch (Exception e) {
			e.printStackTrace();
			throw new JobException(e);
		}
	}

	@Override
	public String toString() {
		return "JobEntry [name=" + name + ", className=" + className + "]";
	}

}
