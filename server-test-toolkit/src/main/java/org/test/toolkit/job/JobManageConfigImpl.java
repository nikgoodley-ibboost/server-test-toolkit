package org.test.toolkit.job;

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

public class JobManageConfigImpl implements JobManageConfig {

	private final static String CONFIG_PATH = "job.xml";

	@Override
	public Collection<ClassEntry<JobManage>> readJobManageEntrys() {
		List<ClassEntry<JobManage>> list = new ArrayList<ClassEntry<JobManage>>();
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

			ClassEntry<JobManage> jobManageEntry = new ClassEntry<JobManage>();
			setJobManageEntry(hashMap, jobManageEntry);
			list.add(jobManageEntry);
		}

		return list;
	}

	public void setJobManageEntry(Map<String, String> hashMap, ClassEntry<JobManage> jobManageEntry) {
		try {
			BeanUtils.copyProperties(jobManageEntry, hashMap);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

}
