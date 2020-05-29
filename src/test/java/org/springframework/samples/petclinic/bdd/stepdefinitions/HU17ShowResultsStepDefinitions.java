package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU17ShowResultsStepDefinitions extends AbstractStep {

	@Then("I see the winner is present")
	public void winnerIsPresent() throws Exception {
		assertEquals("Winner", this.getDriver().findElement(By.xpath("//tr[12]/th")).getText());

		this.stopDriver();
	}

	@Then("I see the winner is not present")
	public void winnerIsNotPresent() throws Exception {
		assertFalse(isElementPresent(By.xpath("//tr[12]/th")));

		this.stopDriver();
	}
}
