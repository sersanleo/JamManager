package org.springframework.samples.petclinic.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.stereotype.Service;

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

		team = this.teamService.findTeamById(20);
		Assertions.assertThat(team).isEqualTo(null);
	}
}
