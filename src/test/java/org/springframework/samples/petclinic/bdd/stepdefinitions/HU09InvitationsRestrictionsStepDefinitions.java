package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertFalse;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.samples.petclinic.model.JamStatus;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU09InvitationsRestrictionsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;
	
	@Then("the invitation from the jam {string} is not present")
	public void invitationNotPresent(String jamName) throws Exception {
		if (JamStatus.valueOf(jamName) != JamStatus.INSCRIPTION) {
			assertFalse(isElementPresent(By.linkText("jamName")));
			
			this.stopDriver();
		}
	}

}
