package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

public class TeamTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@ParameterizedTest
	@CsvSource({
			"'Team Test 1','2015-08-03T12:00:00.0000000'", "'Team Test 2','2015-08-03T12:00:00.0000000'"
	})

	// TODO OK
	void shouldValidateWhenEverythingIsOk(final String name, final LocalDateTime creationDate) {
		Team team = new Team();
		team.setName(name);
		team.setCreationDate(creationDate);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Team>> constraintViolations = validator.validate(team);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	// TODO NULL
	@Test
	void shouldNotValidateWhenNull() {
		Team team = new Team();
		team.setName(null);
		team.setCreationDate(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Team>> constraintViolations = validator.validate(team);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

	// STRING VACÍO
	@Test
	void shouldNotValidateWhenBlank() {
		Team team = new Team();
		team.setName("");
		team.setCreationDate(LocalDateTime.now().minusDays(5));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Team>> constraintViolations = validator.validate(team);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

	// FECHA CREACIÓN EN PASADO
	@Test
	void shouldNotValidateCreationDateInTheFuture() {
		Team team = new Team();
		team.setName("test");
		team.setCreationDate(LocalDateTime.now().plusDays(5));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Team>> constraintViolations = validator.validate(team);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

}
