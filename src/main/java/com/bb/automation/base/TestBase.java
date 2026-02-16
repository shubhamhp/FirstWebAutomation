package com.bb.automation.base;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {

	public static WebDriver driver;
	public static Properties props;
	
	public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();
	
	
	public TestBase(){
		try {
			props = new Properties();
			ClassLoader loader = Thread.currentThread().getContextClassLoader();
			InputStream ip = loader.getResourceAsStream("com/bb/automation/config/config.properties");
			if(ip!=null) {
			props.load(ip);
			}else {
				throw new RuntimeException("Properties file didnt load");
			}
		}catch(NullPointerException e) {
			
		}catch(IOException ie) {
			
		}
	}
	
	
	public static WebDriver getTWebDriver() {
		return tdriver.get();
	}
	public static void initialization() {
		String browserName = props.getProperty("browser");
		System.out.println(browserName);
		if(browserName.equals("Chrome")) {
			driver = new ChromeDriver();
		}
		else if(browserName.equals("Firefox")) {
			driver = new FirefoxDriver();
		}
		
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		long timeOut = Long.parseLong(props.getProperty("timeout"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut));
		
		driver.get(props.getProperty("url"));
	}
	
	
	public static void singeltonInitialization() {
		String browserName = props.getProperty("browser");
		//We check if the driver is null before entering the synchronized block. 
//		This ensures that once the driver is initialized, other tests don't have to wait for a 'lock,' keeping the framework fast.
		//It means after first initialization it doesnt initialize again it fetches already sitting driver.
		if(driver==null) {
		// Only one thread can enter this 'room' at a time
		synchronized(TestBase.class) {
		//The second check inside the block is the final safety net to ensure that if
		//multiple threads were waiting for the lock, they don't all create their own driver instances."
		if(driver==null) {
		if(browserName.equals("Chrome")) {
			driver = new ChromeDriver();
		}
		else if(browserName.equals("Firefox")) {
			driver = new FirefoxDriver();
		}
		}
		}
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		
		long timeOut = Long.parseLong(props.getProperty("timeout"));
		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(timeOut));
		driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeOut));
		
		driver.get(props.getProperty("url"));
	}
	
	/*In this we use getters and setters return tdriver.get() and tdriver.set(new ChromeDriver())*/
	public static void threadInitialization() {
		String browserName = props.getProperty("browser");
		//We check if the driver is null before entering the synchronized block. 
//		This ensures that once the driver is initialized, other tests don't have to wait for a 'lock,' keeping the framework fast.
		//It means after first initialization it doesnt initialize again it fetches already sitting driver.
		if(browserName.equals("Chrome")) {
			tdriver.set(new ChromeDriver());
		}
		else if(browserName.equals("Firefox")) {
			tdriver.set(new FirefoxDriver());
		}
		getTWebDriver().manage().window().maximize();
		Long timeout = Long.parseLong(props.getProperty("timeout"));
		getTWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
		getTWebDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(timeout));
		getTWebDriver().get(props.getProperty("url"));
	}
	
}
