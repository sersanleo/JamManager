package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class HU07InvitacionesUITest {

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

	public HU07InvitacionesUITest asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}

		return this;
	}

	public HU07InvitacionesUITest as(String username, String password) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();

		return this;
	}

	public HU07InvitacionesUITest whenISendAnInvitation(String jamName, String teamName, String invitedUser) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();
		driver.findElement(By.xpath("//a[contains(text(),'Send Invitation')]")).click();
		driver.findElement(By.id("to.username")).click();
		driver.findElement(By.id("to.username")).clear();
		driver.findElement(By.id("to.username")).sendKeys(invitedUser);
		driver.findElement(By.id("add-invitation-form")).submit();

		return this;
	}

	public HU07InvitacionesUITest thenTheInvitationExists(String invitedUser) {
		assertAnyTextEquals(invitedUser, driver.findElements(By.xpath("//table[3]/tbody/tr[*]/td")));

		return this;
	}

	public HU07InvitacionesUITest whenIShowTheTeam(String jamName, String teamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();

		return this;
	}

	public HU07InvitacionesUITest andIDeleteAnInvitation() {
		driver.findElement(By.linkText("Delete Invitation")).click();

		return this;
	}

	public HU07InvitacionesUITest thenTheInvitationNumberDecreases(int originalCount) {
		assertEquals(driver.findElements(By.linkText("Delete Invitation")).size(), originalCount - 1);

		return this;
	}

	public HU07InvitacionesUITest thenButtonIsNotPresent(String buttonText) {
		assertFalse(isElementPresent(By.linkText(buttonText)));

		return this;
	}

	@Test
	@Order(1)
	public void testEnviarInvitacion() throws Exception {
		String invitedUser = "member5";
		as("member2", "member2")
				.whenISendAnInvitation("Inscription Jam", "Grupo 1", "member5")
				.thenTheInvitationExists(invitedUser);
	}

	@Test
	@Order(2)
	public void testBorrarInvitacion() throws Exception {
		as("member2", "member2")
				.whenIShowTheTeam("Inscription Jam", "Grupo 1");

		int numberOfInvitations = driver.findElements(By.linkText("Delete Invitation")).size();

		andIDeleteAnInvitation()
				.thenTheInvitationNumberDecreases(numberOfInvitations);
	}

	// apartir de aqui lo he movido de HU09
	@Test
	@Order(3)
	public void testEnviarInvitacionSinRegistrar() throws Exception {
		asAnonymous()
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenButtonIsNotPresent("Send Invitation");
	}

	@Test
	@Order(4)
	public void testEnviarInvitacionEnOtroGrupo() throws Exception {
		as("member3", "member3")
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenButtonIsNotPresent("Send Invitation");
	}

	@Test
	@Order(5)
	public void testBorrarInvitacionSinRegistrar() throws Exception {
		asAnonymous()
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenButtonIsNotPresent("Delete Invitation");
	}

	@Test
	@Order(6)
	public void testBorrarInvitacionEnOtroGrupo() throws Exception {
		as("member3", "member3")
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenButtonIsNotPresent("Delete Invitation");
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
