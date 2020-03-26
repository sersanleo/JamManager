package org.springframework.samples.petclinic.service;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class JamServiceTests {
	@Autowired
	protected JamService jamService;

	@Test
	void shouldFindJamById() {
		Jam jam = this.jamService.findJamById(1);
		assertThat(jam).isNotEqualTo(null);

		jam = this.jamService.findJamById(20);
		assertThat(jam).isEqualTo(null);
	}

	@Test
	@Transactional
	public void shouldInsertJam() {
		Collection<Jam> jams = this.jamService.findJams();
		int found = jams.size();

		Jam jam = new Jam();
		jam.setName("Test Jam");
		jam.setDescription("This is a test Jam.");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(12);
		jam.setStart(LocalDateTime.now().plusDays(7));
		jam.setEnd(LocalDateTime.now().plusDays(9));
		jam.setRated(false);

		User creator = new User();
		creator.setUsername("jamOrganizator1");
		jam.setCreator(creator);

		this.jamService.saveJam(jam);
		assertThat(jam.getId().longValue()).isNotEqualTo(0);

		jams = this.jamService.findJams();
		assertThat(jams.size()).isEqualTo(found + 1);
	}

	@Test
	@Transactional
	void shouldUpdateJam() {
		Jam jam = this.jamService.findJamById(1);
		String oldName = jam.getName(), newName = oldName + "X";

		jam.setName(newName);
		this.jamService.saveJam(jam);

		jam = this.jamService.findJamById(1);
		assertThat(jam.getName()).isEqualTo(newName);
	}

}
