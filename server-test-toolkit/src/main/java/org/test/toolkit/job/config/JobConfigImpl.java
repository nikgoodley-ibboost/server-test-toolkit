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
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.test.toolkit.job.exception.JobConfigException;
import org.test.toolkit.util.XmlUtil;

public class JobConfigImpl implements JobConfig {

	private final static String CONFIG_PATH = "job.xml";

	@Override
	public Collection<JobEntry<Job>> getJobEntrys() {
		List<JobEntry<Job>> list = new ArrayList<JobEntry<Job>>();
		Document document=null;
		try {
			document = XmlUtil.getDocument(CONFIG_PATH);
		} catch (DocumentException e) {
			throw new JobConfigException(e.getMessage(),e);
 		}

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

			JobEntry<Job> jobEntry = new JobEntry<Job>();
			setJobEntry(hashMap, jobEntry);
			list.add(jobEntry);
		}

		return list;
	}

	public void setJobEntry(Map<String, String> hashMap, JobEntry<Job> jobEntry) {
		try {
			BeanUtils.copyProperties(jobEntry, hashMap);
		} catch (IllegalAccessException e) {
			throw new JobConfigException(e.getMessage(), e);
		} catch (InvocationTargetException e) {
			throw new JobConfigException(e.getMessage(), e);
		}
	}

}
