package org.test.toolkit.util;

public final class MemoryUtil {

	private MemoryUtil() {
 	}
	
	public static long getAvailableMemory(){
		Runtime runtime = Runtime.getRuntime();
		return runtime.maxMemory()-runtime.totalMemory()+runtime.freeMemory();
	}

}
