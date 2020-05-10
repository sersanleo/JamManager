package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU04ShowJamsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@Then("the jam {string} exists")
	public void jamExists(String jamName) throws Exception {
		assertTrue(isElementPresent(By.linkText(jamName)));
		getDriver().findElement(By.linkText(jamName)).click();
		assertEquals(jamName, getDriver().findElement(By.xpath("//b")).getText());
		
		this.stopDriver();
	}
}
