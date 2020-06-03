package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU17ShowResults {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() throws Exception {
		String pathToGeckoDriver = ".\\src\\main\\resources";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public HU17ShowResults asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}

		return this;
	}

	public HU17ShowResults whenIShowTheJam(String jamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();

		return this;
	}

	public HU17ShowResults thenICanSeeTheWinner() {
		assertEquals("Winner", driver.findElement(By.xpath("//tr[12]/th")).getText());

		return this;
	}

	public HU17ShowResults thenICanNotSeeTheWinner() {
		assertFalse(isElementPresent(By.xpath("//tr[12]/th")));

		return this;
	}

	@Test
	public void testSeeFinishedJamWinner() throws Exception {
		asAnonymous()
				.whenIShowTheJam("Finished Jam")
				.thenICanSeeTheWinner();

	}

	@Test
	public void testSeeUnfinishedJamWinner() throws Exception {
		asAnonymous()
				.whenIShowTheJam("In Progress Jam")
				.thenICanNotSeeTheWinner();

	}

	@AfterEach
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	private boolean isElementPresent(By by) {
		try {
			driver.findElement(by);
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	private boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException e) {
			return false;
		}
	}

	private String closeAlertAndGetItsText() {
		try {
			Alert alert = driver.switchTo().alert();
			String alertText = alert.getText();
			if (acceptNextAlert) {
				alert.accept();
			} else {
				alert.dismiss();
			}
			return alertText;
		} finally {
			acceptNextAlert = true;
		}
	}
}
