package org.test.toolkit.util;

import java.io.File;
import java.util.List;

public final class ValidationUtil {
	
	private ValidationUtil(){
		
	}

	/**
	 * Checks for effective list with exception
	 * 
	 * @throws IllegalArgumentException
	 *             
	 */
	public static <T> void effectiveList(List<T> list) {
		if (!isEffectiveList(list)) {
			throw new IllegalArgumentException("list");
		}
	}

	/**
	 * Checks for effective list
	 * 
	 * @return effective
	 * @throws IllegalArgumentException
	 *              
	 */
	public static <T> boolean isEffectiveList(List<T> list) {
		return (list != null && list.size() > 0);
	}

	/**
	 * Checks for exist file with exception
	 * 
	 * @param file
	 * @throws IllegalArgumentException
	 */
	public static void existedFile(File... files) {
		for (File file : files) {
			boolean existedFile = isExistedFile(file);
			if (!existedFile)
				throw new IllegalArgumentException("file path no existed:" + file.getPath());
		}
	}

	public static boolean isExistedFile(File file) {
		return file != null && file.exists();
	}

	/**
	 * Checks for exist file with exception
	 * 
	 * @param file
	 * @throws IllegalArgumentException
	 *             : <li>file path be null or empty <li>file path not found.
	 */
	public static void existedFilePath(String... filePaths) {
		for (String filePath : filePaths) {
			effectiveStr(filePath);
			existedFile(new File(filePath));
		}

	}

	/**
	 * @param filePath
	 * @throws IllegalArgumentException
	 *             : filePath be null or empty
	 * @return
	 */
	public static boolean isExistedFilePath(String filePath) {
		effectiveStr(filePath);
		return isExistedFile(new File(filePath));
	}

	/**
	 * Checks for effective data element with exception
	 * 
	 * @param data
	 */
	public static void effectiveData(byte[] data) {
		if (!isEffectiveData(data)) {
			throw new IllegalArgumentException("illegal data");
		}
	}

	/**
	 * Checks for effective data element
	 * 
	 * @param data
	 */
	public static boolean isEffectiveData(byte[] data) {
		return data != null && data.length > 0;
	}

	/**
	 * @param numbers
	 * @throws IllegalArgumentException
	 */
	public static void effectivePositive(Number... numbers) {
		for (Number number : numbers)
			if (!isEffectivePositive(number))
				throw new IllegalArgumentException("number shouldn't < 0, current number is: " + number);
	}

	/**
	 * Check for the number if be positive(>0)
	 * 
	 * @param number
	 */
	public static boolean isEffectivePositive(Number number) {
		return number.doubleValue() > 0;
	}

	/**
	 * Checks for effective string with exception
	 * 
	 * @param str
	 */
	public static void effectiveStr(String... strs) {
		for (String str : strs)
			if (!isEffectiveStr(str)) {
				throw new IllegalArgumentException("illegal string");
			}
	}

	/**
	 * Checks for effective string with judgments
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isEffectiveStr(String str) {
		return str != null && str.length() > 0;
	}

	public static void nonNull(Object... objects) {
		for (Object object : objects)
			if (isNull(object)) {
				throw new IllegalArgumentException("null object");
			}
	}

	/**
	 * Checks for effective string with judgments
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNull(Object object) {
		return object == null;
	}

}
