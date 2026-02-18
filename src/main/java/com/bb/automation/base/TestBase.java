package com.bb.automation.base;

import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class TestBase {

    public static WebDriver driver;
    public static Properties props;
    
    // ThreadLocal is the best way to run parallel tests
    public static ThreadLocal<WebDriver> tdriver = new ThreadLocal<>();

    // 1. STATIC BLOCK: Loads properties as soon as the class is touched.
    // This prevents "NullPointerException" because we don't need to create an object of TestBase.
    static {
        try {
            props = new Properties();
            // This method works in Eclipse AND inside the JAR file
            ClassLoader loader = Thread.currentThread().getContextClassLoader();
            InputStream ip = loader.getResourceAsStream("config.properties");
            
            if (ip != null) {
                props.load(ip);
            } else {
                throw new RuntimeException("❌ Config.properties not found in Classpath!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static WebDriver getTWebDriver() {
        return tdriver.get();
    }

    // Use this method for your JAR execution (It supports ThreadLocal + Options)
    public static void threadInitialization() {
        
        // 1. Get Browser from Cmd or Config
        String browserName = System.getProperty("browser") != null ? 
                             System.getProperty("browser") : props.getProperty("browser");

        // 2. Headless Detection Logic
        // We activate headless if:
        //   - You pass -Dheadless=true in Command Line
        //   - OR Jenkins is detected (Jenkins always sets an environment variable 'JENKINS_URL')
        boolean isJenkins = System.getenv("JENKINS_URL") != null;
        boolean forceHeadless = System.getProperty("headless", "false").equalsIgnoreCase("true");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        options.addArguments("--disable-search-engine-choice-screen");

        if (isJenkins || forceHeadless) {
            System.out.println("☁️ Environment: Jenkins/Headless Mode Activated");
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1920,1080"); // Critical for Jenkins
            options.addArguments("--disable-gpu");
            options.addArguments("--no-sandbox");
            options.addArguments("--disable-dev-shm-usage");
        } else {
            System.out.println("💻 Environment: Local/UI Mode Activated");
            options.addArguments("--start-maximized");
        }

        // 3. Initialize with Options
        if (browserName.equalsIgnoreCase("Chrome")) {
            tdriver.set(new ChromeDriver(options));
        } else if (browserName.equalsIgnoreCase("Firefox")) {
            // You can add FirefoxOptions here too if needed
            tdriver.set(new FirefoxDriver());
        }

        // Standard Setup
        getTWebDriver().manage().deleteAllCookies();
        long timeout = Long.parseLong(props.getProperty("timeout"));
        getTWebDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(timeout));
        getTWebDriver().get(props.getProperty("url"));
    }

}