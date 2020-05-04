package org.springframework.samples.petclinic.ui;

import java.util.regex.Pattern;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
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
@DirtiesContext
public class DeliveryUITest {
	
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
  public void testCreateDelivery() throws Exception {
	  	testLogInAsMember();
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
  }



  private void testLogInAsMember() throws Exception {
    driver.get("http://localhost:" +port +"/");
    driver.findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
    driver.findElement(By.id("username")).clear();
    driver.findElement(By.id("username")).sendKeys("member1");
    driver.findElement(By.id("password")).clear();
    driver.findElement(By.id("password")).sendKeys("member1");
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
