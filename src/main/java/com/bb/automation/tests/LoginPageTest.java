package com.bb.automation.tests;

import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import com.bb.automation.base.TestBase;
import com.bb.automation.pages.HomePage;
import com.bb.automation.pages.LoginPage;
import com.bb.automation.testUtils.*;

@Listeners(WebEvenListener.class)
public class LoginPageTest extends TestBase{
	
	// Declare Page Objects
    LoginPage loginPage;
    HomePage homePage;

    // CONSTRUCTOR: Force the TestBase constructor to run so config is loaded
	public LoginPageTest() {
		super();
	}
	
	
	@BeforeMethod
	public void setUp() {
		threadInitialization();
		loginPage = new LoginPage();
	}
	
	
	@Test
	public void loginTest() {
		loginPage.login();
	}
	
	
	@AfterMethod
	public void tearDown() {
		getTWebDriver().quit();
	}

}
