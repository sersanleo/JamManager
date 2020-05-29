package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
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
public class HU13EnvioEntregasUITest {

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

	public HU13EnvioEntregasUITest asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
		return this;
	}

	public HU13EnvioEntregasUITest as(String username, String password) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU13EnvioEntregasUITest whenICreateADelivery(String jamName, String teamName, String description,
			String downloadUrl) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();
		driver.findElement(By.linkText("New Delivery")).click();
		driver.findElement(By.id("downloadURL")).click();
		driver.findElement(By.id("downloadURL")).clear();
		driver.findElement(By.id("downloadURL")).sendKeys(downloadUrl);
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(description);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU13EnvioEntregasUITest thenADeliveryIsCreated(String description, String downloadUrl) {
		assertAnyTextEquals(description, driver.findElements(By.xpath("//tr[*]/td[2]")));
		assertTrue(isElementPresent(By.linkText(downloadUrl)));

		return this;
	}

	public HU13EnvioEntregasUITest whenIShowTheTeam(String jamName, String teamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();

		return this;
	}

	public HU13EnvioEntregasUITest andIDeleteADelivery() {
		driver.findElement(By.linkText("Delete Delivery")).click();

		return this;
	}

	public HU13EnvioEntregasUITest thenTheDeliveriesNumberDecreases(int originalCount) {
		assertEquals(driver.findElements(By.linkText("Delete Delivery")).size(), originalCount - 1);

		return this;
	}

	public HU13EnvioEntregasUITest thenErrorIsDisplayed(String error, int fieldOrder) {
		assertEquals(error,
				driver.findElement(By.xpath("//form[@id='add-delivery-form']/div/div[" + fieldOrder + "]/div/span[2]"))
						.getText());

		return this;
	}

	public HU13EnvioEntregasUITest thenButtonIsNotPresent(String buttonText) {
		assertFalse(isElementPresent(By.linkText(buttonText)));

		return this;
	}

	@Order(1)
	@Test
	public void testCreateDelivery() throws Exception {
		String description = "una", downloadUrl = "http://www.google.com/";
		as("member1", "member1")
				.whenICreateADelivery("In Progress Jam", "Grupo 1", description, downloadUrl)
				.thenADeliveryIsCreated(description, downloadUrl);
	}

	@Order(2)
	@Test
	public void testDeleteDelivery() throws Exception {
		as("member1", "member1")
				.whenIShowTheTeam("In Progress Jam", "Grupo 1");

		int deliveriesCount = driver.findElements(By.linkText("Delete Delivery")).size();

		andIDeleteADelivery()
				.thenTheDeliveriesNumberDecreases(deliveriesCount);
	}

	@Test
	@Order(3)
	public void testCreateDeliveryURLNull() throws Exception {
		as("member1", "member1")
				.whenICreateADelivery("In Progress Jam", "Grupo 1", "", "")
				.thenErrorIsDisplayed("no puede estar vacío", 1);
	}

	@Order(4)
	@Test
	public void testCreateDeliveryInvalidUrl() throws Exception {
		as("member1", "member1")
				.whenICreateADelivery("In Progress Jam", "Grupo 1", "Una buena descripcion", "no es una url")
				.thenErrorIsDisplayed("tiene que ser una URL válida", 1);
	}

	@Test
	@Order(5)
	public void testCreateDeliveryNotMember() throws Exception {
		as("judge1", "judge1")
				.whenIShowTheTeam("In Progress Jam", "Grupo 1")
				.thenButtonIsNotPresent("New Delivery");

	}

	@Test
	@Order(6)
	public void testDeleteDeliveryNotMember() throws Exception {
		as("judge1", "judge1")
				.whenIShowTheTeam("In Progress Jam", "Grupo 1")
				.thenButtonIsNotPresent("Delete Delivery");

	}

	@Test
	@Order(7)
	public void testDeleteDeliveryNotInProgress() throws Exception {
		as("member1", "member1")
				.whenIShowTheTeam("Rating Jam", "Grupo 1")
				.thenButtonIsNotPresent("Delete Delivery");
	}

	@Test
	@Order(8)
	public void testCreateDeliveryNotInProgress() throws Exception {
		as("member1", "member1")
				.whenIShowTheTeam("Rating Jam", "Grupo 1")
				.thenButtonIsNotPresent("New Delivery");
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

	private static final void assertAnyTextEquals(String text, List<WebElement> webElements) {
		for (WebElement webElement : webElements) {
			if (text.equals(webElement.getText()))
				return;
		}
		assert false;
	}
}
