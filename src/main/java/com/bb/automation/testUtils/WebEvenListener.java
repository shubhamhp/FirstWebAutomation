package com.bb.automation.testUtils;

import java.io.IOException;

import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.bb.automation.base.TestBase;
import com.bb.automation.reports.ExtentReportsManager;

public class WebEvenListener extends TestBase implements ITestListener{

	//Call the engine
	private static ExtentReports extent = ExtentReportsManager.createInstance();
	//Thread Safe logger box
	private static ThreadLocal<ExtentTest> test = new ThreadLocal<>();
	
	@Override
	public void onTestFailure(ITestResult result) {

		System.out.println("Failed test " +result.getName());
		test.get().fail(result.getThrowable());
		try {
			String path = Utilities.takeScreenShotWhenFailed();
			test.get().addScreenCaptureFromPath(path);
			 
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	 public void onTestStart(ITestResult result) {
		  
		 ExtentTest extentTest = extent.createTest(result.getMethod().getMethodName());
		 test.set(extentTest);
		  }

		  /**
		   * Invoked each time a test succeeds.
		   *
		   * @param result <code>ITestResult</code> containing information about the run test
		   * @see ITestResult#SUCCESS
		   */
		 public void onTestSuccess(ITestResult result) {
		    // not implemented
			 test.get().log(Status.PASS,"Test Passed");
		  }

		  /**
		   * Invoked each time a test is skipped.
		   *
		   * @param result <code>ITestResult</code> containing information about the run test
		   * @see ITestResult#SKIP
		   */
		  public void onTestSkipped(ITestResult result) {
		    // not implemented
			  test.get().log(Status.SKIP,"Test Skipped");
		  }
		  
		  public void onFinish(ITestContext context) {
			    // not implemented
			  if(extent!=null) {
				  extent.flush();
			  }
			  }
}
