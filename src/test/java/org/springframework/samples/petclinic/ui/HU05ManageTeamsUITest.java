package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
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
public class HU05ManageTeamsUITest {

	@LocalServerPort
	private int port;

	private WebDriver driver;
	private String baseUrl;
	private boolean acceptNextAlert = true;
	private StringBuffer verificationErrors = new StringBuffer();

	@BeforeEach
	public void setUp() {
		String pathToGeckoDriver = ".\\src\\main\\resources";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver + "\\geckodriver.exe");
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	public HU05ManageTeamsUITest asAnonymous() {
		driver.get("http://localhost:" + port);
		WebElement element = driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			driver.findElement(By.linkText("Logout")).click();
			driver.findElement(By.xpath("//button[@type='submit']")).click();
		}
		return this;
	}

	public HU05ManageTeamsUITest as(String username, String password) {
		driver.get("http://localhost:" + port);
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys(password);
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys(username);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public HU05ManageTeamsUITest whenICreateATeam(String jamName, String teamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText("Join this Jam")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(teamName);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public HU05ManageTeamsUITest whenIShowTheJam(String jamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		return this;
	}

	public HU05ManageTeamsUITest whenIShowTheTeam(String jamName, String teamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();

		return this;
	}

	public HU05ManageTeamsUITest thenTheCreateTeamButtonIsNotPresent() {
		assertFalse(isElementPresent(By.linkText("Join this Jam")));
		return this;
	}

	public HU05ManageTeamsUITest thenTheEditTeamButtonIsNotPresent() {
		assertFalse(isElementPresent(By.linkText("Edit Team")));
		return this;
	}

	public HU05ManageTeamsUITest thenTheDeleteTeamButtonIsNotPresent() {
		assertFalse(isElementPresent(By.linkText("Delete Member")));
		return this;
	}

	public HU05ManageTeamsUITest thenTheTeamIsCreated(String teamName) {
		assertEquals(teamName, driver.findElement(By.xpath("//b")).getText());
		return this;
	}

	public HU05ManageTeamsUITest whenIEditATeamInJam(String jamName, String teamName, String newTeamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();
		driver.findElement(By.linkText("Edit Team")).click();
		driver.findElement(By.id("name")).click();
		driver.findElement(By.id("name")).clear();
		driver.findElement(By.id("name")).sendKeys(newTeamName);
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		return this;
	}

	public HU05ManageTeamsUITest thenTheTeamIsEdited(String newTeamName) {
		assertEquals(newTeamName, driver.findElement(By.xpath("//b")).getText());
		return this;
	}

	public HU05ManageTeamsUITest whenIDeleteATeam(String jamName, String teamName) {
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		driver.findElement(By.linkText(jamName)).click();
		driver.findElement(By.linkText(teamName)).click();
		driver.findElement(By.linkText("Delete Member")).click();
		return this;
	}

	public HU05ManageTeamsUITest thenTheTeamIsDeleted(String teamName) {
		assertFalse(isElementPresent(By.linkText(teamName)));
		return this;
	}

	public HU05ManageTeamsUITest thenTheBlankTeamIsNotCreated() {
		assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='add-team-form']/div/div/div/span[2]")).getText());
		return this;
	}

	@Test
	@Order(1)
	public void testCrearNuevoTeam() {
		String teamName = "equipo";
		as("member1", "member1")
				.whenICreateATeam("Inscription Jam", teamName)
				.thenTheTeamIsCreated(teamName);
	}

	@Test
	@Order(2)
	public void testEditarTeam() {
		String teamName = "equipo", newTeamName = "nuevo equipo";
		as("member1", "member1")
				.whenIEditATeamInJam("Inscription Jam", teamName, newTeamName)
				.thenTheTeamIsEdited(newTeamName);
	}

	@Test
	@Order(3)
	public void testDeleteTeam() {
		String teamName = "nuevo equipo";
		as("member1", "member1")
				.whenIDeleteATeam("Inscription Jam", teamName)
				.thenTheTeamIsDeleted(teamName);
	}

	@Test
	@Order(4)
	public void testCreateBlankTeam() {
		as("member1", "member1")
				.whenICreateATeam("Inscription Jam", "")
				.thenTheBlankTeamIsNotCreated();
	}

	@Test
	@Order(5)
	public void testAnonymousCantCreateTeams() {
		asAnonymous()
				.whenIShowTheJam("Inscription Jam")
				.thenTheCreateTeamButtonIsNotPresent();
	}

	@Test
	@Order(6)
	public void testAnonymousCantEditTeams() {
		asAnonymous()
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenTheEditTeamButtonIsNotPresent();
	}

	@Test
	@Order(7)
	public void testAnonymousCantDeleteTeams() {
		asAnonymous()
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenTheDeleteTeamButtonIsNotPresent();
	}

	@Test
	@Order(8)
	public void testBorrarOtroTeam() {
		as("member1", "member1")
				.whenIShowTheTeam("Inscription Jam", "Grupo 1")
				.thenTheDeleteTeamButtonIsNotPresent();
	}

	@Test
	@Order(9)
	public void testCrearTeamJamFueraDeInscripcion() {
		as("member2", "member2")
				.whenIShowTheJam("Cancelled Jam")
				.thenTheCreateTeamButtonIsNotPresent();
	}

	@Test
	@Order(10)
	public void testEditarTeamJamFueraDeInscripcion() {
		as("member2", "member2")
				.whenIShowTheTeam("In Progress Jam", "Grupo 2")
				.thenTheDeleteTeamButtonIsNotPresent();
	}

	@AfterEach
	public void tearDown() {
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
