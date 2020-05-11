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
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
public class DeliveryUITest {

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
		System.setProperty("webdriver.firefox.driver", pathToGeckoDriver + "\\geckodriver.exe");
		driver = new FirefoxDriver();
		baseUrl = "https://www.google.com/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	@Order(1)
	public void testCreateDelivery() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		driver.findElement(By.linkText("New Delivery")).click();
		driver.findElement(By.id("downloadURL")).click();
		driver.findElement(By.id("downloadURL")).clear();
		driver.findElement(By.id("downloadURL")).sendKeys("http://www.youtube.com/");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("una");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		LogOut();
	}

	@Order(2)
	@Test
	public void testDeleteDelivery() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//a[contains(@href, '/jams')]")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		driver.findElement(By.linkText("Delete Delivery")).click();
		LogOut();
	}

	@Order(4)
	@Test
	public void testCreateDeliveryInvalidUrl() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		driver.findElement(By.linkText("New Delivery")).click();
		driver.findElement(By.id("downloadURL")).click();
		driver.findElement(By.id("downloadURL")).clear();
		driver.findElement(By.id("downloadURL")).sendKeys("no es una url");
		driver.findElement(By.id("description")).clear();
		driver.findElement(By.id("description")).sendKeys("Una buena descripcion");
		driver.findElement(By.id("add-delivery-form")).submit();
		assertEquals("tiene que ser una URL válida",
				driver.findElement(By.xpath("//form[@id='add-delivery-form']/div/div/div/span[2]")).getText());
		LogOut();
	}

	@Test
	@Order(3)
	public void testCreateDeliveryURLNull() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		driver.findElement(By.linkText("New Delivery")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
		assertEquals("no puede estar vacío",
				driver.findElement(By.xpath("//form[@id='add-delivery-form']/div/div/div/span[2]")).getText());
		LogOut();
	}

	@Test
	@Order(5)
	public void testCreateDeliveryNotMember() throws Exception {
		LogInAsJudge();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("New Delivery")));
		LogOut();
	}

	@Test
	@Order(6)
	public void testDeleteDeliveryNotMember() throws Exception {
		LogInAsJudge();
		;
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("In Progress Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("Delete Delivery")));
		LogOut();
	}

	@Test
	@Order(7)
	public void testDeleteDeliveryNotInProgress() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Rating Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("Delete Delivery")));
		LogOut();
	}

	@Test
	@Order(8)
	public void testCreateDeliveryNotInProgress() throws Exception {
		LogInAsMember();
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Rating Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("New Delivery")));
		LogOut();
	}

	private void LogInAsMember() throws Exception {
		driver.get("http://localhost:" + port + "/");
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("member1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("member1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	private void LogInAsJudge() throws Exception {
		driver.get("http://localhost:" + port + "/");
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		driver.findElement(By.id("username")).clear();
		driver.findElement(By.id("username")).sendKeys("judge1");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("judge1");
		driver.findElement(By.xpath("//button[@type='submit']")).click();
	}

	private void LogOut() throws Exception {
		driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		driver.findElement(By.linkText("Logout")).click();
		driver.findElement(By.xpath("//button[@type='submit']")).click();
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
