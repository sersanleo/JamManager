
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.samples.petclinic.web.JamValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class JamTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	private JamValidator createJamValidator() {
		return new JamValidator();
	}

	@ParameterizedTest
	@CsvSource({
		"'Jam Test 1','test',5,'2025-06-03T12:00:00.0000000',5,2,5,'2025-07-03T12:00:00.0000000','2025-08-03T12:00:00.0000000'", "'Jam Test 2','test',2,'2025-06-03T12:00:00.0000000',1,3,3,'2025-07-03T12:00:00.0000000','2025-08-03T12:00:00.0000000'"
	})
	void shouldValidateWhenEverythingIsOk(final String name, final String description, final Integer difficulty, final LocalDateTime inscriptionDeadline, final Integer maxTeamSize, final Integer minTeams, final Integer maxTeams, final LocalDateTime start,
		final LocalDateTime end) {
		Jam jam = new Jam();
		jam.setName(name);
		jam.setDescription(description);
		jam.setDifficulty(difficulty);
		jam.setInscriptionDeadline(inscriptionDeadline);
		jam.setMaxTeamSize(maxTeamSize);
		jam.setMinTeams(minTeams);
		jam.setMaxTeams(maxTeams);
		jam.setStart(start);
		jam.setEnd(end);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenNull() {
		Jam jam = new Jam();
		jam.setName(null);
		jam.setDescription(null);
		jam.setDifficulty(null);
		jam.setInscriptionDeadline(null);
		jam.setMaxTeamSize(null);
		jam.setMinTeams(null);
		jam.setMaxTeams(null);
		jam.setStart(null);
		jam.setEnd(null);
		jam.setRated(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(10);
	}

	@Test
	void shouldNotValidateWhenBlank() {
		Jam jam = new Jam();
		jam.setName("");
		jam.setDescription("");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0, 6
	})
	void shouldNotValidateDifficultyOutOfRange(final int difficulty) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(difficulty);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Jam> cv = constraintViolations.iterator().next();
		Assertions.assertThat(cv.getPropertyPath().toString()).isEqualTo("difficulty");
		Assertions.assertThat(cv.getMessage()).isEqualTo("must be between 1 and 5");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0
	})
	void shouldNotValidateMaxTeamSizeOutOfRange(final int maxTeamSize) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(3);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(maxTeamSize);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Jam> cv = constraintViolations.iterator().next();
		Assertions.assertThat(cv.getPropertyPath().toString()).isEqualTo("maxTeamSize");
		Assertions.assertThat(cv.getMessage()).isEqualTo("must be greater than or equal to 1");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0, 1
	})
	void shouldNotValidateMinTeamsOutOfRange(final int minTeams) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(3);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(minTeams);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Jam> cv = constraintViolations.iterator().next();
		Assertions.assertThat(cv.getPropertyPath().toString()).isEqualTo("minTeams");
		Assertions.assertThat(cv.getMessage()).isEqualTo("must be greater than or equal to 2");
	}

	@ParameterizedTest
	@ValueSource(ints = {
		-1, 0, 1
	})
	void shouldNotValidateMaxTeamsOutOfRange(final int maxTeams) {
		LocaleContextHolder.setLocale(Locale.ENGLISH);

		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(3);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(maxTeams);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Jam>> constraintViolations = validator.validate(jam);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<Jam> cv = constraintViolations.iterator().next();
		Assertions.assertThat(cv.getPropertyPath().toString()).isEqualTo("maxTeams");
		Assertions.assertThat(cv.getMessage()).isEqualTo("must be greater than or equal to 2");
	}

	@Test
	void shouldNotValidateMinGreaterThanMax() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(3);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(11);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("minTeams")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("minTeams").getCode()).isEqualTo("The minimum number of teams must be lower than the maximum");
	}

	@Test
	void shouldNotValidateInscriptionAfterStart() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(3);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(4));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("inscriptionDeadline")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("inscriptionDeadline").getCode()).isEqualTo("The inscription deadline must be before the start of the event");
	}

	@Test
	void shouldNotValidateStartAfterEnd() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(8));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("start")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("start").getCode()).isEqualTo("The start of the event must be before the end of itself");
	}

	@Test
	void shouldNotValidateInscriptionDeadlineInThePastWhenNew() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().plusDays(7));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getErrorCount()).isEqualTo(1);
		Assertions.assertThat(errors.getFieldErrorCount("inscriptionDeadline")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("inscriptionDeadline").getCode()).isEqualTo("The inscription deadline must be in the future");
	}

	@Test
	void shouldNotValidateStartInThePastWhenNew() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().minusDays(7));
		jam.setEnd(LocalDateTime.now().plusDays(8));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getFieldErrorCount("start")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("start").getCode()).isEqualTo("The start must be in the future");
	}

	@Test
	void shouldNotValidateEndInThePastWhenNew() {
		Jam jam = new Jam();
		jam.setName("test");
		jam.setDescription("test");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(5));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(10);
		jam.setStart(LocalDateTime.now().plusDays(6));
		jam.setEnd(LocalDateTime.now().minusDays(7));

		JamValidator jamValidator = this.createJamValidator();

		BindException errors = new BindException(jam, "jam");
		ValidationUtils.invokeValidator(jamValidator, jam, errors);

		Assertions.assertThat(errors.getFieldErrorCount("end")).isEqualTo(1);
		Assertions.assertThat(errors.getFieldError("end").getCode()).isEqualTo("The end must be in the future");
	}
}
