package com.cisco.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;

public class User {

	public static void main(String[] args) throws HttpException, IOException {
		if (validation()) {
			executeAdd();
		}
	}

	private static boolean validation() {
		boolean result = true;
		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(new File("C:/user.txt")));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split("=");
				if (split.length != 3) {
					System.out
							.println("format error: must make sure every line matched with \"name=sex=nianji\", but line:"
									+ line);
					result = false;
					break;
				}
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e) {
			}
		}
	}

	private static void executeAdd() throws FileNotFoundException {
		System.out.println("begin to add student....");
		BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("C:/user.txt")));
		try {
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				String[] split = line.split("=");
				String name = split[0].trim();
 				String sex = split[1].trim().equals("ÄÐ") || split[1].trim().equals("1") ? "nan"
						: "nv";
				String nianji = split[2].trim();
				addUser(name, sex, nianji);
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (bufferedReader != null)
					bufferedReader.close();
			} catch (Exception e) {
			}
		}

		System.out.println("================complete==================");

		try {
			TimeUnit.DAYS.sleep(1);
		} catch (InterruptedException e) {
 			e.printStackTrace();
		}
	}

	private static void addUser(String name, String sex, String nianji) {
		HttpClient httpClient = new HttpClient();
		PostMethod postMethod = new PostMethod(
				"http://192.168.3.148/PsyTestWeb/SchoolSystem/UserSettings/AddUsers.aspx");
		// PostMethod postMethod = new PostMethod("http://www.baidu.com");
		try {
			NameValuePair[] array = getNameValuePairs(name, nianji, sex);
			postMethod.setRequestBody(array);
			postMethod.addRequestHeader("Accept-Language", "zh-cn");
			postMethod.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "UTF-8");
			int statusCode = httpClient.executeMethod(postMethod);
			if (isFail(statusCode))
				System.out.println("add student fail==============: " + name);
		} catch (Exception e) {
 			System.out.println("add student fail============: " + name);
		}
	}

	private static boolean isFail(int statusCode) {
		return statusCode != 200 && statusCode != 201 && statusCode != 204;
	}

	private static NameValuePair[] getNameValuePairs(String name, String nianji, String sex)
			throws UnsupportedEncodingException {
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		nameValuePairs.add(new NameValuePair("name", name));
		nameValuePairs.add(new NameValuePair("sex", sex));
		nameValuePairs.add(new NameValuePair("txtBeginTime", "2000/01/01"));
		nameValuePairs.add(new NameValuePair("number", "168"));
		nameValuePairs.add(new NameValuePair("utype", "1"));
		nameValuePairs.add(new NameValuePair("password", "pass"));
		nameValuePairs.add(new NameValuePair("password2", "pass"));
		nameValuePairs.add(new NameValuePair("kexi", "12"));
		nameValuePairs.add(new NameValuePair("nianji", nianji));
		nameValuePairs.add(new NameValuePair("email", ""));
		nameValuePairs.add(new NameValuePair("mobile", ""));

		nameValuePairs.add(new NameValuePair("__EVENTTARGET", ""));
		nameValuePairs.add(new NameValuePair("__EVENTARGUMENT", ""));
		nameValuePairs.add(new NameValuePair("__LASTFOCUS", ""));
		nameValuePairs
				.add(new NameValuePair(
						"__VIEWSTATE",
						"/wEPDwULLTE2NTU3NDkwNjYPZBYCAgMPZ"
								+ "BYEAhsPZBYCZg9kFgICAQ8QDxYCHgtfIURhdGFCb3VuZGdkEBUCCy3or7fpgInmi6ktEOWGm+iurei/"
								+ "numYnzIwMTIVAgExAjEyFCsDAmdnFgECAWQCHQ9kFgJmD2QWAgIBDxAPFgYeDURhdGFUZXh0RmllbGQFB"
								+ "ENsYXMeDkRhdGFWYWx1ZUZpZWxkBQJJZB8AZ2QQFRQBMQEyATMBNAE1ATYBNwE4ATkCMTACMTECMTICMTMCM"
								+ "TQCMTUCMTYCMTcCMTgCMTkCMjAVFAI1OAI1OQI2MAI2MQI2MgI2MwI2NAI2NQI2NgI2NwI2OAI2OQI3MAI3MQI3MgI3"
								+ "MwI3NAI3NQI3NgI3NxQrAxRnZ2dnZ2dnZ2dnZ2dnZ2dnZ2dnZ2RkGAEFHl9fQ29udHJvbHNSZXF1aXJlUG9zdEJhY2t"
								+ "LZXlfXxYEBQNuYW4FAm52BQJudgUKcmVnQnV0dGlvbkIDp9Galyiladrxiel49yVQjyAA"));

		nameValuePairs
				.add(new NameValuePair(
						"__EVENTVALIDATION",
						"/wEWJgLK3ZgKAseX4qUDAsj4yMsPAsj4gMgPAtighK8NAtigiK8NAtmg5K"
								+ "wNAtmg6KwNAtmg7KwNAtmg0KwNAtmg1KwNAtmg2KwNAtmg3KwNAtmgwKwNAtmghK8NAtmgiK8NAtqg5KwNAtqg6KwNAtqg7KwNAtqg0KwNAtqg"
								+ "1KwNAtqg2KwNAtqg3KwNAtqgwKwNAvu49B0C2bWgVwKN+IStAwLdwIe2CALsqoOEBgKMmfitCwKNmfitCwKOmfitCwKPmfitCwLyveCR"
								+ "DwKt9JiEDQKyzcaDDQKlusiVBwKOrsh0NOG+HrLr32U6MB2PNSpzt1KEnLc="));

		nameValuePairs.add(new NameValuePair("regButtion.x", "35"));
		nameValuePairs.add(new NameValuePair("regButtion.y", "6"));

		NameValuePair[] array = {};
		return nameValuePairs.toArray(array);
	}

}
