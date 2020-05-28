package org.springframework.samples.petclinic.ui;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;


import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
public class HU06RestringirTeamsUITest {

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

	//

	@Test
	@Order(1)
	public void testCrearTeamSinLoguearse() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Inscription Jam")).click();
		assertFalse(isElementPresent(By.linkText("Join this Jam")));
	}
	
	@Test
	@Order(2)
	  public void testCrearTeamSiYaParticipas() throws Exception {
	    driver.get("http://localhost:8080/");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("member2");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("member2");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Inscription Jam")).click();
	    assertFalse(isElementPresent(By.linkText("Join this Jam")));
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }
	
	@Test
	@Order(3)
	public void testEditarTeamSinLoguearse() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Inscription Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("Edit Team")));
	}
	
	@Test
	@Order(4)
	public void testBorrarTeamSinLoguearse() throws Exception {
		driver.get("http://localhost:8080/");
		driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
		driver.findElement(By.linkText("Inscription Jam")).click();
		driver.findElement(By.linkText("Grupo 1")).click();
		assertFalse(isElementPresent(By.linkText("Delete Member")));
	}
	
	
	@Test
	@Order(5)
	  public void testBorrarOtroTeam() throws Exception {
	    driver.get("http://localhost:8080/");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("member1");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("member1");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Inscription Jam")).click();
	    driver.findElement(By.linkText("Grupo 1")).click();
	    assertFalse(isElementPresent(By.linkText("Delete Member")));
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }
	
	
	@Test
	@Order(6)
	  public void testCrearTeamJamFueraDeInscripcion() throws Exception {
	    driver.get("http://localhost:8080/");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("member2");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("member2");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Cancelled Jam")).click();
	    assertFalse(isElementPresent(By.linkText("Join this Jam")));
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }
	
	
	@Test
	@Order(7)
	  public void testEditarTeamJamFueraDeInscripcion() throws Exception {
	    driver.get("http://localhost:8080/");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.id("username")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("member2");
	    driver.findElement(By.id("password")).click();
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("member2");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("In Progress Jam")).click();
	    driver.findElement(By.linkText("Grupo 2")).click();
	    assertFalse(isElementPresent(By.linkText("Delete Member")));
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.linkText("Logout")).click();
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	  }


	//

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