
package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class AbstractStep {
	private static WebDriver driver;
	private static StringBuffer verificationErrors = new StringBuffer();

	public WebDriver getDriver() {
		if (driver == null) {
			String pathToGeckoDriver = ".\\src\\main\\resources";
			System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
			driver = new FirefoxDriver();
			driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
		}
		return driver;
	}

	public void stopDriver() {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
		driver = null;
	}
	
	protected boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}
}