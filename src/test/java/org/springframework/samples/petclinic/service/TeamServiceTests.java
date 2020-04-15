package org.springframework.samples.petclinic.service;

import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
public class TeamServiceTests {

	@Autowired
	protected TeamService teamService;

	@Autowired
	protected JamService jamService;

	// findById()
	@Test
	void shouldFindTeamById() {
		Team team = this.teamService.findTeamById(1);
		Assertions.assertThat(team).isNotEqualTo(null);
	}

	// findById()
	@Test
	void shouldNotFindTeamById() {

		Assertions.assertThatThrownBy(() -> this.teamService.findTeamById(20))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void shouldFindIsMemberOfTeamByJamIdAndUsername() {
		Assertions.assertThat(this.teamService.findIsMemberOfTeamByJamIdAndUsername(1, "member2")).isEqualTo(true);
		Assertions.assertThat(this.teamService.findIsMemberOfTeamByJamIdAndUsername(1, "member4")).isEqualTo(false);
	}

	@Test
	void shouldFindIsMemberOfTeamByTeamIdAndUsername() {
		Assertions.assertThat(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(1, "member2")).isEqualTo(true);
		Assertions.assertThat(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(1, "member1")).isEqualTo(false);
	}

	@Test
	@Transactional
	void shouldUpdateTeam() {
		Team team = this.teamService.findTeamById(1);
		String oldName = team.getName(), newName = oldName + "X";

		team.setName(newName);
		this.teamService.saveTeam(team);

		team = this.teamService.findTeamById(1);

		Assertions.assertThat(team.getName()).isEqualTo(newName);
	}

	@Test
	@Transactional
	void shouldSaveTeam() throws Exception {
		Jam jam = this.jamService.findJamById(1);
		int found = jam.getTeams().size();

		User memberUser = new User();
		memberUser.setUsername("member5");

		Team team = new Team();
		team.setName("test team");
		team.setJam(jam);
		Set<User> members = new HashSet<>();
		members.add(memberUser);
		team.setMembers(members);
		jam.getTeams().add(team);

		this.teamService.saveTeam(team);

		jam = this.jamService.findJamById(1);
		Assertions.assertThat(jam.getTeams().size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	void shouldDeleteTeamWhenSavingEmptyTeam() throws Exception {
		Team team = this.teamService.findTeamById(1);
		Assertions.assertThat(team).isNotNull();

		team.getMembers().clear();
		this.teamService.saveTeam(team);

		Assertions.assertThatThrownBy(() -> this.teamService.findTeamById(1))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	@Transactional
	void shouldDeleteTeam() throws Exception {
		Team team = this.teamService.findTeamById(2);
		Assertions.assertThat(team).isNotNull();

		this.teamService.deleteTeam(team);

		Assertions.assertThatThrownBy(() -> this.teamService.findTeamById(2))
				.isInstanceOf(NoSuchElementException.class);
	}
}
