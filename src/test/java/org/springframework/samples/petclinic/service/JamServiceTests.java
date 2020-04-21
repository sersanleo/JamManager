
package org.springframework.samples.petclinic.service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.util.EntityUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class JamServiceTests {

	@Autowired
	protected JamService jamService;

	@Test
	void shouldFindJamById() {
		Assertions.assertThat(this.jamService.findJamById(1)).isNotEqualTo(null);
	}

	@Test
	void shouldNotFindJamByInexistentId() {
		Assertions.assertThatThrownBy(() -> this.jamService.findJamById(20)).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void shouldFindAllJams() {
		Collection<Jam> jams = this.jamService.findJams();

		Jam jam = EntityUtils.getById(jams, Jam.class, 1);
		Assertions.assertThat(jam.getName()).isEqualTo("Inscription Jam");
		jam = EntityUtils.getById(jams, Jam.class, 5);
		Assertions.assertThat(jam.getName()).isEqualTo("Finished Jam");
	}

	@Test
	@Transactional
	public void shouldInsertJamAndGenerateId() {
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
		Assertions.assertThat(jam.getId()).isNotNull();

		jams = this.jamService.findJams();
		Assertions.assertThat(jams.size()).isEqualTo(found + 1);
	}

	@Test
	public void shouldNotInsertNullJam() {
		Assertions.assertThatThrownBy(() -> this.jamService.saveJam(null)).isInstanceOf(Exception.class);
	}

	@Test
	@Transactional
	void shouldUpdateJam() {
		Jam jam = this.jamService.findJamById(1);
		String oldName = jam.getName(), newName = oldName + "X";

		jam.setName(newName);
		this.jamService.saveJam(jam);

		jam = this.jamService.findJamById(1);
		Assertions.assertThat(jam.getName()).isEqualTo(newName);
	}

	@Test
	@Transactional
	void shouldDeleteJam() {
		Jam jam = this.jamService.findJamById(1);
		this.jamService.deleteJam(jam);

		Assertions.assertThatThrownBy(() -> this.jamService.findJamById(1)).isInstanceOf(NoSuchElementException.class);
	}

	@Test
	void shouldNotDeleteNullJam() {
		Assertions.assertThatThrownBy(() -> this.jamService.deleteJam(null)).isInstanceOf(Exception.class);
	}

}
