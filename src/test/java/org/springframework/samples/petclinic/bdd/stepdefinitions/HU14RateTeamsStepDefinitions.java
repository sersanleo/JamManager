package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU14RateTeamsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@Then("the rate button is not present")
	public void rateButtonNotPresent() throws Exception {
		assertFalse(isElementPresent(By.linkText("Give A Mark")));

		this.stopDriver();
	}

	@Then("the rate button is present")
	public void rateButtonPresent() throws Exception {
		assertTrue(isElementPresent(By.linkText("Give A Mark")));

		this.stopDriver();
	}
}
