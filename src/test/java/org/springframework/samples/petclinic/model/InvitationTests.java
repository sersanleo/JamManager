package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class InvitationTests {

	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@BeforeEach
	private void beforeEach() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}

	@Test
	void shouldValidateWhenEverythingIsOk() {
		Invitation invitation = new Invitation();

		invitation.setCreationDate(LocalDateTime.now().minusNanos(1));
		invitation.setFrom(new Team());
		invitation.setStatus(InvitationStatus.PENDING);
		invitation.setTo(new User());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Invitation>> constraintViolations = validator.validate(invitation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenEverythingIsNull() {
		Invitation invitation = new Invitation();

		invitation.setCreationDate(null);
		invitation.setFrom(null);
		invitation.setStatus(null);
		invitation.setTo(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Invitation>> constraintViolations = validator.validate(invitation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

	@Test
	void shouldNotValidateWhenCreationDateIsInTheFuture() {
		Invitation invitation = new Invitation();

		invitation.setCreationDate(LocalDateTime.now().plusDays(1));
		invitation.setFrom(new Team());
		invitation.setStatus(InvitationStatus.PENDING);
		invitation.setTo(new User());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Invitation>> constraintViolations = validator.validate(invitation);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

}
