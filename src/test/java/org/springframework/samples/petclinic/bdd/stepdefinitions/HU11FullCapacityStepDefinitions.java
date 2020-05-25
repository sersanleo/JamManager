package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU11FullCapacityStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;
	
	@Then("the join button is not present")
	public void buttonNotPresent() {
		assertFalse(isElementPresent(By.linkText("Join this Jam")));
		
		this.stopDriver();
	}

}
