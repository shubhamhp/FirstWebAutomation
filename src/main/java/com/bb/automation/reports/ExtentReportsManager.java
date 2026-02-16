package com.bb.automation.reports;

import java.io.File;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

public class ExtentReportsManager {

	public static ExtentReports extent;
	
	public static ExtentReports createInstance() {
		//Save file directory
		File reportName = new File(System.getProperty("user.dir")+ "/test-reports/ExtentReports.html");
		
		//Printer -- Spark
		ExtentSparkReporter spark = new ExtentSparkReporter(reportName);
		spark.config().setTheme(Theme.DARK);
		spark.config().setDocumentTitle("WebAutomation");
		spark.config().setReportName("Regression");
		
		//engine to create
		extent = new ExtentReports();
		extent.attachReporter(spark);
		
		extent.setSystemInfo("Tester", "Sp");
		extent.setSystemInfo("Env", "QA");
				
		return extent;
		
	}
}
