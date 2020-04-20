
package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class MarkServiceTests {

	@Autowired
	protected MarkService markService;

	/*@Test
	void shouldFindByTeamIdAndJudgeUsername() {
		Mark mark = this.markService.findByTeamIdAndJudgeUsername(1, "judge1");
		Assertions.assertThat(mark).isNotEqualTo(null);

		Assertions.assertThatThrownBy(() -> this.markService.findByTeamIdAndJudgeUsername(1, "nonExistentJudge"))
				.isInstanceOf(NoSuchElementException.class);
	}*/

	// Hacer las pruebas
}