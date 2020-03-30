package org.springframework.samples.petclinic.model;

import java.util.Locale;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class JamResourceTests {
	private Validator createValidator() {
		LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
		localValidatorFactoryBean.afterPropertiesSet();
		return localValidatorFactoryBean;
	}

	@BeforeEach
	private void beforeEach() {
		LocaleContextHolder.setLocale(Locale.ENGLISH);
	}

	@ParameterizedTest
	@CsvSource({ "'Descripcion 1', 'https://www.youtube.com/'",
			"'Description 2 Algo mas larga', 'https://www.google.com/intl/es_ALL/drive/'" })
	void shouldValidateWhenEverythingIsOk(final String description, final String downloadUrl) {
		JamResource jamResource = new JamResource();

		jamResource.setDescription("Description");
		jamResource.setDownloadUrl(downloadUrl);
		jamResource.setJam(new Jam());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<JamResource>> constraintViolations = validator.validate(jamResource);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenEverythingIsNull() {
		JamResource jamResource = new JamResource();

		jamResource.setDescription(null);
		jamResource.setDownloadUrl(null);
		jamResource.setJam(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<JamResource>> constraintViolations = validator.validate(jamResource);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(2);
	}

	@Test
	void shouldNotValidateWhenDescriptionIsEmpty() {
		JamResource jamResource = new JamResource();

		jamResource.setDescription("");
		jamResource.setDownloadUrl("https://www.youtube.com/");
		jamResource.setJam(new Jam());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<JamResource>> constraintViolations = validator.validate(jamResource);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<JamResource> restriction = constraintViolations.iterator().next();
		Assertions.assertThat(restriction.getPropertyPath().toString()).isEqualTo("description");
		Assertions.assertThat(restriction.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void shouldNotValidateWhenUrlIsEmpty() {
		JamResource jamResource = new JamResource();

		jamResource.setDescription("Test Description");
		jamResource.setDownloadUrl("");
		jamResource.setJam(new Jam());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<JamResource>> constraintViolations = validator.validate(jamResource);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<JamResource> restriction = constraintViolations.iterator().next();
		Assertions.assertThat(restriction.getPropertyPath().toString()).isEqualTo("downloadUrl");
		Assertions.assertThat(restriction.getMessage()).isEqualTo("must not be blank");
	}

	@Test
	void shouldNotValidateWhenUrlIsNotAnUrl() {
		JamResource jamResource = new JamResource();

		jamResource.setDescription("Test Description");
		jamResource.setDownloadUrl("No es una url");
		jamResource.setJam(new Jam());

		Validator validator = this.createValidator();
		Set<ConstraintViolation<JamResource>> constraintViolations = validator.validate(jamResource);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
		ConstraintViolation<JamResource> restriction = constraintViolations.iterator().next();
		Assertions.assertThat(restriction.getPropertyPath().toString()).isEqualTo("downloadUrl");
		Assertions.assertThat(restriction.getMessage()).isEqualTo("must be a valid URL");
	}
}