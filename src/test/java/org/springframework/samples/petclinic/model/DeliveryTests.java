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

class DeliveryTests {

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
		Delivery delivery= new Delivery();

		delivery.setCreationDate(LocalDateTime.now().minusNanos(1));
		delivery.setDownloadURL("https://www.youtube.com/");
		delivery.setDescription("Una buena descripcion");
		delivery.setTeam(new Team());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(delivery);
		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenEverythingIsNull() {
		Delivery delivery= new Delivery();

		delivery.setCreationDate(null);
		delivery.setDownloadURL(null);
		delivery.setDescription(null);
		delivery.setTeam(null);


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(delivery);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

	@Test
	void shouldNotValidateWhenCreationDateIsInTheFuture() {
		Delivery delivery= new Delivery();

		delivery.setCreationDate(LocalDateTime.now().plusDays(2));
		delivery.setDownloadURL("https://www.youtube.com/");
		delivery.setDescription("Una buena descripcion");
		delivery.setTeam(new Team());


		Validator validator = this.createValidator();
		Set<ConstraintViolation<Delivery>> constraintViolations = validator.validate(delivery);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

}
