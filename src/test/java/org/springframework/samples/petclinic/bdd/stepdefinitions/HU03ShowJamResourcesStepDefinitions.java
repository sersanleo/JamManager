package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Then;
import lombok.extern.java.Log;

@Log
public class HU03ShowJamResourcesStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@Then("I can see the resources")
	public void editButtonNotPresent() throws Exception {
		assertEquals("Resources", getDriver().findElement(By.xpath("//div/b")).getText());

		this.stopDriver();
	}

	@Then("I can not see the resources")
	public void deleteButtonNotPresent() throws Exception {
		assertNotEquals("Resources", getDriver().findElement(By.xpath("//div/b")).getText());

		this.stopDriver();
	}
}
