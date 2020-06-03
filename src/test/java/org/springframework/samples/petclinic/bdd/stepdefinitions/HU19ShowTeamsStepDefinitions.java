package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU19ShowTeamsStepDefinitions extends AbstractStep {

	@And("I show the team {string} details")
	public void winnerIsPresent(String teamName) throws Exception {
		this.getDriver().findElement(By.linkText(teamName)).click();
	}

	@Then("I can see its deliveries and marks")
	public void deliveriesAndMarksPresent() throws Exception {
		assertEquals("https://www.us.es/", this.getDriver().findElement(By.linkText("https://www.us.es/")).getText());
	    assertEquals("judge1", this.getDriver().findElement(By.xpath("//table[4]/tbody/tr[2]/td")).getText());
		
		this.stopDriver();
	}

	@Then("I can not see its deliveries and marks")
	public void deliveriesAndMarksNotPresent() throws Exception {
		assertFalse(isElementPresent(By.linkText("https://www.us.es/")));
		assertFalse(isElementPresent(By.xpath("//table[4]/tbody/tr[2]/td")));
		
		this.stopDriver();
	}
}
