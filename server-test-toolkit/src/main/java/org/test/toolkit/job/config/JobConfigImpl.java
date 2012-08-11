package org.test.toolkit.job.config;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.test.toolkit.util.XmlUtil;

public class JobConfigImpl implements JobConfig {

	private final static String CONFIG_PATH = "job.xml";

	@Override
	public Collection<JobEntry<Job>> getJobSourceEntrys() {
		List<JobEntry<Job>> list = new ArrayList<JobEntry<Job>>();
		Document document = XmlUtil.getDocument(CONFIG_PATH);

		@SuppressWarnings("unchecked")
		List<Element> jobs = document.selectNodes("//job");
		for (Element job : jobs) {
			Map<String, String> hashMap = new HashMap<String, String>();
			@SuppressWarnings("unchecked")
			List<Element> paramsElements = job.elements("parameter");
			for (Element paramsElement : paramsElements) {
				Attribute key = paramsElement.attribute("name");
				Attribute value = paramsElement.attribute("value");
				hashMap.put(key.getValue(), value.getValue());
			}

			JobEntry<Job> jobSourceEntry = new JobEntry<Job>();
			setJobSourceEntry(hashMap, jobSourceEntry);
			list.add(jobSourceEntry);
		}

		return list;
	}

	public void setJobSourceEntry(Map<String, String> hashMap, JobEntry<Job> jobManageEntry) {
		try {
			BeanUtils.copyProperties(jobManageEntry, hashMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
