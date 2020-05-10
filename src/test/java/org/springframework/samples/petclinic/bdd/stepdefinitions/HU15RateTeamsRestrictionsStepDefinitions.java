package org.springframework.samples.petclinic.bdd.stepdefinitions;

import org.openqa.selenium.By;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.And;
import lombok.extern.java.Log;

@Log
public class HU15RateTeamsRestrictionsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@And("rate a team with a value of {string} and comments {string}")
	public void rateATeam(String value, String comments) throws Exception {
		getDriver().findElement(By.linkText("Give A Mark")).click();
		getDriver().findElement(By.id("value")).clear();
		getDriver().findElement(By.id("value")).sendKeys(value);
		getDriver().findElement(By.id("comments")).clear();
		getDriver().findElement(By.id("comments")).sendKeys(comments);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}
}
