package com.bb.automation.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.testng.Assert;

import com.bb.automation.base.TestBase;

public class LoginPage extends TestBase {

	public LoginPage() {
		// "this" means: Initialize elements in THIS class using the driver from TestBase.
		PageFactory.initElements(getTWebDriver(), this);
	}
	
	@FindBy(id="user-name")
	WebElement username;
	
	@FindBy(id="password")
	WebElement password;
	
	@FindBy(id="login-button")
	WebElement loginBtn;
	
	
	public void login() {
		username.sendKeys(props.getProperty("username"));
		password.sendKeys(props.getProperty("password"));
		loginBtn.click();
//		Assert.assertEquals("1", "4");
	}
	
}
