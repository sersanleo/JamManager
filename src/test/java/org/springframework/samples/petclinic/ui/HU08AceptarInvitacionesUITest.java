package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
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
@TestMethodOrder(OrderAnnotation.class)
public class HU08AceptarInvitacionesUITest {

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

	public HU08AceptarInvitacionesUITest asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}

		return this;
	}

	public HU08AceptarInvitacionesUITest as(String username, String password) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU08AceptarInvitacionesUITest whenIListMyInvitations() {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[3]/a/span[2]")).click();

		return this;
	}

	public HU08AceptarInvitacionesUITest andIRejectAnInvitation() {
		driver.findElement(By.linkText("Reject Invitation")).click();

		return this;
	}

	public HU08AceptarInvitacionesUITest andIAcceptAnInvitation() {
		driver.findElement(By.linkText("Accept Invitation")).click();

		return this;
	}

	public HU08AceptarInvitacionesUITest thenTheInvitationNumberDecreases(int originalCount) {
		assertEquals(driver.findElements(By.linkText("Reject Invitation")).size(), originalCount - 1);

		return this;
	}

	@Test
	@Order(1)
	public void testRechazarInvitacion() throws Exception {
		as("member1", "member1")
				.whenIListMyInvitations();

		int numberOfInvitations = driver.findElements(By.linkText("Accept Invitation")).size();

		andIRejectAnInvitation()
				.thenTheInvitationNumberDecreases(numberOfInvitations);
	}

	@Test
	@Order(2)
	public void testAceptarInvitacion() throws Exception {
		as("member1", "member1")
				.whenIListMyInvitations();

		int numberOfInvitations = driver.findElements(By.linkText("Accept Invitation")).size();

		andIAcceptAnInvitation()
				.thenTheInvitationNumberDecreases(numberOfInvitations);
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
