package org.test.toolkit.timerjob.mbean;

import java.lang.management.ManagementFactory;

import javax.management.InstanceAlreadyExistsException;
import javax.management.MBeanRegistrationException;
import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;

public class JmxMonitorImpl implements JmxMonitor {
	
	private final static MBeanServer MBEAN_SERVER=ManagementFactory.getPlatformMBeanServer();
	private final static JmxMonitor INSTANCE=new JmxMonitorImpl();
	
 	public static JmxMonitor getInstance() {
		return INSTANCE;
	}

	private JmxMonitorImpl(){
		
	}
	
	public void registerMBean(Object object, ObjectName name){
		try {
			MBEAN_SERVER.registerMBean(object, name);
		} catch (InstanceAlreadyExistsException e) {
 			e.printStackTrace();
		} catch (MBeanRegistrationException e) {
 			e.printStackTrace();
		} catch (NotCompliantMBeanException e) {
 			e.printStackTrace();
		}
	} 
   
 }
