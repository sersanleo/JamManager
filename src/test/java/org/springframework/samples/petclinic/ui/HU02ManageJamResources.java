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
public class HU02ManageJamResources {

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

	public HU02ManageJamResources asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
		return this;
	}

	public HU02ManageJamResources as(String username, String password) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU02ManageJamResources whenICreateAResource(String jamName, String resourceDescription, String resourceUrl) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText("Add New Resource")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(resourceDescription);
		driver.findElement(By.id("downloadUrl")).click();
		driver.findElement(By.id("downloadUrl")).clear();
		driver.findElement(By.id("downloadUrl")).sendKeys(resourceUrl);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU02ManageJamResources whenIEditAResource(String jamName, String newDescription, String newUrl) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText("Edit Jam Resource")).click();
		driver.findElement(By.id("description")).click();
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys(newDescription);
		driver.findElement(By.id("downloadUrl")).click();
		driver.findElement(By.id("downloadUrl")).clear();
		driver.findElement(By.id("downloadUrl")).sendKeys(newUrl);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU02ManageJamResources whenIShowTheJam(String jamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();

		return this;
	}

	public HU02ManageJamResources andIDeleteAResource() {
		driver.findElement(By.linkText("Delete Jam Resource")).click();

		return this;
	}

	public HU02ManageJamResources thenTheResourceIsPresent(String resourceDescription, String resourceUrl) {
		assertAnyTextEquals(resourceDescription, driver.findElements(By.xpath("//tr[*]/td[2]")));
		assertTrue(isElementPresent(By.linkText(resourceUrl)));

		return this;
	}

	public HU02ManageJamResources thenTheResourcesNumberDecreases(int originalCount) {
		assertEquals(driver.findElements(By.linkText("Delete Jam Resource")).size(), originalCount - 1);

		return this;
	}

	public HU02ManageJamResources thenErrorIsDisplayed(String error, int fieldOrder) {
		assertEquals(error,
				driver.findElement(By.xpath("//form[@id='jamResource']/div/div[" + fieldOrder + "]/div/span[2]"))
						.getText());

		return this;
	}

	public HU02ManageJamResources thenButtonIsNotPresent(String buttonText) {
		assertFalse(isElementPresent(By.linkText(buttonText)));

		return this;
	}

	@Test
	@Order(1)
	public void testCreateJamResource() throws Exception {
		String resourceDescription = "Una buena descripcion", resourceUrl = "https://www.youtube2.com/";

		as("jamOrganizator1", "jamOrganizator1")
				.whenICreateAResource("Pending Jam", resourceDescription, resourceUrl)
				.thenTheResourceIsPresent(resourceDescription, resourceUrl);
	}

	@Test
	@Order(2)
	public void testDeleteJamResource() throws Exception {
		as("jamOrganizator1", "jamOrganizator1")
				.whenIShowTheJam("Pending Jam");

		int numberOfResources = driver.findElements(By.linkText("Delete Jam Resource")).size();

		andIDeleteAResource()
				.thenTheResourcesNumberDecreases(numberOfResources);

	}

	@Test
	@Order(3)
	public void testEditJamResource() throws Exception {
		String resourceDescription = "Una buena descripcion editada", resourceUrl = "https://www.discord.com";

		as("jamOrganizator1", "jamOrganizator1")
				.whenIEditAResource("Pending Jam", resourceDescription, resourceUrl)
				.thenTheResourceIsPresent(resourceDescription, resourceUrl);
	}

	@Test
	@Order(4)
	public void testCreateJamResourceNotDownloadURL() throws Exception {
		as("jamOrganizator1", "jamOrganizator1")
				.whenICreateAResource("Pending Jam", "Una buena descripcion", "")
				.thenErrorIsDisplayed("no puede estar vacío", 2);
	}

	@Test
	@Order(5)
	public void testCreateJamResourceNotDescription() throws Exception {
		as("jamOrganizator1", "jamOrganizator1")
				.whenICreateAResource("Pending Jam", "", "https://www.youtube.com/")
				.thenErrorIsDisplayed("no puede estar vacío", 1);
	}

	@Test
	@Order(6)
	public void testCreateJamResourceInvalidDownloadURL() throws Exception {
		as("jamOrganizator1", "jamOrganizator1")
				.whenICreateAResource("Pending Jam", "Una buena descripcion", "esto no es una url")
				.thenErrorIsDisplayed("tiene que ser una URL válida", 2);

	}

	@Test
	@Order(7)
	public void testEditJamResourceNotJamOrganizator() throws Exception {
		as("member1", "member1")
				.whenIShowTheJam("Pending Jam")
				.thenButtonIsNotPresent("Edit Jam Resource");

	}

	@Test
	@Order(8)
	public void testCreateJamResourceNotJamOrganizator() throws Exception {
		as("member1", "member1")
				.whenIShowTheJam("Pending Jam")
				.thenButtonIsNotPresent("Add New Resource");

	}

	@Test
	@Order(9)
	public void testDeleteJamResourceNotJamOrganizator() throws Exception {
		as("member1", "member1")
				.whenIShowTheJam("Pending Jam")
				.thenButtonIsNotPresent("Delete Jam Resource");
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
