package com.bb.automation.testUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
//import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.bb.automation.base.TestBase;

public class Utilities extends TestBase {

	public void waitForElementToBeVisible(By by,int time) {
		WebDriverWait wait = new WebDriverWait(getTWebDriver(), Duration.ofSeconds(time));
		wait.until(ExpectedConditions.invisibilityOfElementLocated(by));
	}
	
	
	public static String takeScreenShotWhenFailed() throws IOException {
		
		 File screenshotFile = ((TakesScreenshot) getTWebDriver()).getScreenshotAs(OutputType.FILE);
		 String timeStamp = new SimpleDateFormat("YYYY_mm_dd_hh_mm_ss").format(new Date());
		 String currentDir = System.getProperty("user.dir");
		 String path = currentDir +"/screenshots/"+timeStamp+".png";
		 File dest = new File(path);
		 FileUtils.copyFile(screenshotFile, dest);
		 return path;
	}
	
}
