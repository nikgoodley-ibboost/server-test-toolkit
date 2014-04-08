package com.googlecode.test.toolkit.casewrapper.testng;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

import org.testng.IAnnotationTransformer;
import org.testng.IRetryAnalyzer;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.annotations.ITestAnnotation;

public class TestRetryListener  implements IAnnotationTransformer,ITestListener  {

	@SuppressWarnings("rawtypes")
	@Override
	public void transform(ITestAnnotation annotation, Class clazz,
			Constructor constructor, Method method) {
		IRetryAnalyzer retry = annotation.getRetryAnalyzer();
		if (retry == null) {
			annotation.setRetryAnalyzer(TestRetryAnalyzer.class);
		}
	}

	@Override
	public void onTestStart(ITestResult paramITestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSuccess(ITestResult paramITestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailure(ITestResult paramITestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestSkipped(ITestResult paramITestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(
			ITestResult paramITestResult) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStart(ITestContext paramITestContext) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onFinish(ITestContext paramITestContext) {
		// TODO Auto-generated method stub
		
	}
}