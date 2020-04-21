
package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class MarkServiceTests {

	@Autowired
	protected MarkService markService;

	@Test
	void shouldFindByTeamIdAndJudgeUsername() {
		Mark mark = this.markService.findByTeamIdAndJudgeUsername(8, "judge1");
		Assertions.assertThat(mark).isNotEqualTo(null);
	}

	@Test
	void shouldNotFindByTeamIdAndJudgeUsernameWhenInexistent() {
		Assertions.assertThatThrownBy(() -> this.markService.findByTeamIdAndJudgeUsername(1, "nonExistentJudge"))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	@Transactional
	void shouldInsertMarkAndGenerateId() {
		Mark mark = new Mark();
		mark.setValue(5f);
		mark.setComments("");

		User judge = new User();
		judge.setUsername("judge2");
		mark.setJudge(judge);

		Team team = new Team();
		team.setId(6);
		mark.setTeam(team);

		this.markService.saveMark(mark);
		Assertions.assertThat(mark.getId()).isNotNull();

		Mark insertedMark = this.markService.findByTeamIdAndJudgeUsername(6, "judge2");
		Assertions.assertThat(insertedMark).isNotNull();
		Assertions.assertThat(insertedMark.getId()).isEqualTo(mark.getId());
	}

	@Test
	void shouldNotInsertNullMark() {
		Assertions.assertThatThrownBy(() -> this.markService.saveMark(null)).isInstanceOf(Exception.class);
	}

	@Test
	@Transactional
	void shouldUpdateMark() {
		Mark mark = this.markService.findByTeamIdAndJudgeUsername(8, "judge1");
		String oldComments = mark.getComments(), newComments = oldComments + "X";
		mark.setComments(newComments);

		this.markService.saveMark(mark);

		mark = this.markService.findByTeamIdAndJudgeUsername(8, "judge1");
		Assertions.assertThat(mark.getComments()).isEqualTo(newComments);
	}
}
