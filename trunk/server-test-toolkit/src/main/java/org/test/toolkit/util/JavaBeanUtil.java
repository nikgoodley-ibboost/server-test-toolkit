package org.test.toolkit.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author fu.jian
 *
 */
public class JavaBeanUtil {

	public static <T> T toJavaBean(Map<String, ?> map, Class<T> type)
			throws IntrospectionException, IllegalAccessException, InstantiationException,
			InvocationTargetException {
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		T obj = type.newInstance();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();

			if (map.containsKey(propertyName)) {
				Object value = map.get(propertyName);
				descriptor.getWriteMethod().invoke(obj, value);
			}
		}
		return obj;
	}

	public static Map<String, Object> toMap(Object bean) throws IntrospectionException,
			IllegalAccessException, InvocationTargetException {

		Class<?> type = bean.getClass();
		BeanInfo beanInfo = Introspector.getBeanInfo(type);
		PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
		Map<String, Object> returnMap = new HashMap<String, Object>();
		for (int i = 0; i < propertyDescriptors.length; i++) {
			PropertyDescriptor descriptor = propertyDescriptors[i];
			String propertyName = descriptor.getName();
			if (!propertyName.equals("class")) {
				Method readMethod = descriptor.getReadMethod();
				Object result = readMethod.invoke(bean, new Object[0]);
				if (result != null) {
					returnMap.put(propertyName, result);
				} else {
					returnMap.put(propertyName, "");
				}
			}
		}
		return returnMap;
	}
}
