package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU12CancelJamsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@Then("I see the jam is cancelled")
	public void jamIsCancelled() throws Exception {
		assertEquals("CANCELLED", getDriver().findElement(By.xpath("//tr[10]/td")).getText());
		
		this.stopDriver();
	}

	@Then("I see the jam is not cancelled")
	public void jamIsNotCancelled() throws Exception {
		assertNotEquals("CANCELLED", getDriver().findElement(By.xpath("//tr[10]/td")).getText());

		this.stopDriver();
	}
}
