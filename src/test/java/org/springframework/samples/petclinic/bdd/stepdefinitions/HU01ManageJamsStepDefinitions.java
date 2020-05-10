package org.springframework.samples.petclinic.bdd.stepdefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.java.Log;

@Log
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HU01ManageJamsStepDefinitions extends AbstractStep {
	@LocalServerPort
	private int port;

	@Given("I am not logged in the system")
	public void IamNotLogged() throws Exception {
		getDriver().get("http://localhost:" + port);
		WebElement element = getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a"));
		if (element == null || !element.getText().equalsIgnoreCase("login")) {
			getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a/span[2]")).click();
			getDriver().findElement(By.linkText("Logout")).click();
			getDriver().findElement(By.xpath("//button[@type='submit']")).click();
		}
	}

	@Given("I am logged in the system as {string} with password {string}")
	public void IdoLoginAs(String username, String password) throws Exception {
		getDriver().get("http://localhost:" + port);
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul[2]/li/a")).click();
		getDriver().findElement(By.id("password")).clear();
		getDriver().findElement(By.id("password")).sendKeys(password);
		getDriver().findElement(By.id("username")).clear();
		getDriver().findElement(By.id("username")).sendKeys(username);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@When("I list jams")
	public void listJams() throws Exception {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
	}

	@Then("the create button is not present")
	public void createButtonNotPresent() throws Exception {
		assertFalse(isElementPresent(By.linkText("Add Jam")));

		this.stopDriver();
	}

	@When("I create a new Jam with name {string}, description {string}, difficulty {string}, inscription deadline {string},	max team size {string}, min. teams {string}, max. teams {string}, start {string} and end {string}")
	public void createJam(String name, String description, String difficulty, String inscriptionDeadline,
			String maxTeamSize, String minTeams, String maxTeams, String start, String end) throws Exception {
		getDriver().findElement(By.xpath("//div[@id='main-navbar']/ul/li[2]/a/span[2]")).click();
		getDriver().findElement(By.linkText("Add Jam")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys(name);
		getDriver().findElement(By.id("description")).clear();
		getDriver().findElement(By.id("description")).sendKeys(description);
		getDriver().findElement(By.id("difficulty")).clear();
		getDriver().findElement(By.id("difficulty")).sendKeys(difficulty);
		getDriver().findElement(By.id("inscriptionDeadline")).clear();
		getDriver().findElement(By.id("inscriptionDeadline")).sendKeys(inscriptionDeadline);
		getDriver().findElement(By.id("maxTeamSize")).clear();
		getDriver().findElement(By.id("maxTeamSize")).sendKeys(maxTeamSize);
		getDriver().findElement(By.id("minTeams")).clear();
		getDriver().findElement(By.id("minTeams")).sendKeys(minTeams);
		getDriver().findElement(By.id("maxTeams")).clear();
		getDriver().findElement(By.id("maxTeams")).sendKeys(maxTeams);
		getDriver().findElement(By.id("start")).clear();
		getDriver().findElement(By.id("start")).sendKeys(start);
		getDriver().findElement(By.id("end")).clear();
		getDriver().findElement(By.id("end")).sendKeys(end);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the jam {string} is created")
	public void jamIsCreated(String name) throws Exception {
		assertEquals(name, getDriver().findElement(By.xpath("//b")).getText());
		listJams();
		assertTrue(isElementPresent(By.linkText(name)));

		this.stopDriver();
	}

	@When("I change the jam name {string} to {string}")
	public void renameJam(String oldName, String newName) throws Exception {
		listJams();
		getDriver().findElement(By.linkText(oldName)).click();
		getDriver().findElement(By.linkText("Edit Jam")).click();
		getDriver().findElement(By.id("name")).clear();
		getDriver().findElement(By.id("name")).sendKeys(newName);
		getDriver().findElement(By.xpath("//button[@type='submit']")).click();
	}

	@Then("the name is changed from {string} to {string}")
	public void jamIsCreated(String oldName, String newName) throws Exception {
		assertEquals(newName, getDriver().findElement(By.xpath("//b")).getText());
		listJams();
		assertTrue(isElementPresent(By.linkText(newName)));
		assertFalse(isElementPresent(By.linkText(oldName)));

		this.stopDriver();
	}

	@When("I delete the jam named {string}")
	public void deleteJam(String name) throws Exception {
		listJams();
		getDriver().findElement(By.linkText(name)).click();
		getDriver().findElement(By.linkText("Delete Jam")).click();
	}

	@Then("the jam {string} is deleted")
	public void jamIsRemoved(String name) throws Exception {
		assertFalse(isElementPresent(By.linkText(name)));

		this.stopDriver();
	}

	@When("I am viewing the jam {string} details")
	public void viewJamDetails(String jamName) throws Exception {
		listJams();
		getDriver().findElement(By.linkText(jamName)).click();
	}

	@Then("the edit button is not present")
	public void editButtonNotPresent() throws Exception {
		assertFalse(isElementPresent(By.linkText("Edit Jam")));

		this.stopDriver();
	}

	@Then("the delete button is not present")
	public void deleteButtonNotPresent() throws Exception {
		assertFalse(isElementPresent(By.linkText("Delete Jam")));

		this.stopDriver();
	}
}
