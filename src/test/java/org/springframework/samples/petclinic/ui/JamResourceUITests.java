package org.springframework.samples.petclinic.ui;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestMethodOrder(OrderAnnotation.class)
@DirtiesContext
public class JamResourceUITests {

	 @LocalServerPort
	  private int port;
	  
	  private WebDriver driver;
	  private String baseUrl;
	  private boolean acceptNextAlert = true;
	  private StringBuffer verificationErrors = new StringBuffer();

	  @BeforeEach
	  public void setUp() throws Exception {
		String pathToGeckoDriver="C:\\Users\\Usuario\\Desktop\\Workspace DP-2 Definitivo\\Gecko";
		System.setProperty("webdriver.gecko.driver", pathToGeckoDriver +"\\geckodriver.exe");
	    driver = new FirefoxDriver();
	    baseUrl = "https://www.google.com/";
	    driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	  }
	  
	  @Test
	  @Order(1)
	  public void testCreateJamResource() throws Exception {
	    LoginAsJamOrganizator();
	    driver.findElement(By.linkText("Jams")).click();;
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.linkText("Add New Resource")).click();
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Una buena descripcion");
	    driver.findElement(By.id("downloadUrl")).click();
	    driver.findElement(By.id("downloadUrl")).clear();
	    driver.findElement(By.id("downloadUrl")).sendKeys("https://www.youtube.com/");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    LogOut();
	  }
	  
	  @Test
	  @Order(2)
	  public void testDeleteJamResource() throws Exception {
		LoginAsJamOrganizator();
		driver.findElement(By.linkText("Jams")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.xpath("(//a[contains(text(),'Delete Jam Resource')])[2]")).click();
	    LogOut();
	  }
	  
	  @Test
	  @Order(3)
	  public void testEditJamResource() throws Exception {
		  LoginAsJamOrganizator();
		driver.findElement(By.linkText("Jams")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.linkText("Edit Jam Resource")).click();
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Una buena descripcion editada");
	    driver.findElement(By.id("downloadUrl")).click();
	    driver.findElement(By.id("downloadUrl")).clear();
	    driver.findElement(By.id("downloadUrl")).sendKeys("https://www.discord.com");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    LogOut();
	  }
	  
	  @Test
	  @Order(4)
	  public void testCreateJamResourceNotDownloadURL() throws Exception {
	    LoginAsJamOrganizator();
	    driver.findElement(By.linkText("Jams")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.linkText("Add New Resource")).click();
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Una buena descripcion");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='jamResource']/div/div[2]/div/span[2]")).getText());
	    LogOut();
	  }
	  
	  @Test
	  @Order(5)
	  public void testCreateJamResourceNotDescription() throws Exception {
	    LoginAsJamOrganizator();
	    driver.findElement(By.linkText("Jams")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.linkText("Add New Resource")).click();
	    driver.findElement(By.id("downloadUrl")).click();
	    driver.findElement(By.id("downloadUrl")).clear();
	    driver.findElement(By.id("downloadUrl")).sendKeys("https://www.youtube.com/");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("no puede estar vacío", driver.findElement(By.xpath("//form[@id='jamResource']/div/div/div/span[2]")).getText());
	    LogOut();
	  }

	  @Test
	  @Order(6)
	  public void testCreateJamResourceInvalidDownloadURL() throws Exception {
	    LoginAsJamOrganizator();
	    driver.findElement(By.linkText("Jams")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    driver.findElement(By.linkText("Add New Resource")).click();
	    driver.findElement(By.id("description")).click();
	    driver.findElement(By.id("description")).clear();
	    driver.findElement(By.id("description")).sendKeys("Una buena descripcion");
	    driver.findElement(By.id("downloadUrl")).clear();
	    driver.findElement(By.id("downloadUrl")).sendKeys("esto no es una url");
	    driver.findElement(By.xpath("//button[@type='submit']")).click();
	    assertEquals("tiene que ser una URL válida", driver.findElement(By.xpath("//form[@id='jamResource']/div/div[2]/div/span[2]")).getText());
	    LogOut();
	  }
	  
	  @Test
	  @Order(7)
	  public void testEditJamResourceNotJamOrganizator() throws Exception {
	    LogInAsMember();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    assertFalse(isElementPresent(By.linkText("Edit Jam Resource")));
	    LogOut();
	  }
	  
	  @Test
	  @Order(8)
	  public void testCreateJamResourceNotJamOrganizator() throws Exception {
	    LogInAsMember();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    assertFalse(isElementPresent(By.linkText("Add New Resource")));
	    LogOut();
	  }

	  @Test
	  @Order(9)
	  public void testDeleteJamResourceNotJamOrganizator() throws Exception {
	    LogInAsMember();
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a")).click();
	    driver.findElement(By.linkText("Pending Jam")).click();
	    assertFalse(isElementPresent(By.linkText("Delete Jam Resource")));
	    LogOut();
	  }
	  
	  private void LogInAsMember() throws Exception {
		    driver.get("http://localhost:" +port +"/");
		    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		    driver.findElement(By.id("username")).clear();
		    driver.findElement(By.id("username")).sendKeys("member1");
		    driver.findElement(By.id("password")).clear();
		    driver.findElement(By.id("password")).sendKeys("member1");
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
		  }
	  
	  private void LogOut() throws Exception {
		    driver.findElement(By.xpath("//a[contains(@href, '#')]")).click();
		    driver.findElement(By.linkText("Logout")).click();
		    driver.findElement(By.xpath("//button[@type='submit']")).click();
		  }
	  
	private void LoginAsJamOrganizator() throws Exception {
		driver.get("http://localhost:" +port +"/");
	    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
	    driver.findElement(By.id("username")).clear();
	    driver.findElement(By.id("username")).sendKeys("jamOrganizator1");
	    driver.findElement(By.id("password")).clear();
	    driver.findElement(By.id("password")).sendKeys("jamOrganizator1");
	    driver.findElement(By.id("password")).sendKeys(Keys.ENTER);
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
}}
