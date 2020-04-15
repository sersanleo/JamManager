
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
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

class MarkTests {

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
	@CsvSource({
			"0.5,'test'",
			"10,''",
			"0,''",
			"5.28,'anotherTest'"
	})
	void shouldValidateWhenEverythingIsOk(final Float value, final String comments) {
		Mark mark = new Mark();
		mark.setValue(value);
		mark.setComments(comments);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Mark>> constraintViolations = validator.validate(mark);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(0);
	}

	@Test
	void shouldNotValidateWhenNull() {
		Mark mark = new Mark();
		mark.setValue(null);
		mark.setComments(null);

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Mark>> constraintViolations = validator.validate(mark);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}

	@ValueSource(floats = { -1, 11, -5, 15 })
	@ParameterizedTest
	void shouldNotValidateWhenValueNotInRange(final float value) {
		Mark mark = new Mark();
		mark.setValue(value);
		mark.setComments("");

		Validator validator = this.createValidator();
		Set<ConstraintViolation<Mark>> constraintViolations = validator.validate(mark);

		Assertions.assertThat(constraintViolations.size()).isEqualTo(1);
	}
}
