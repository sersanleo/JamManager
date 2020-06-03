package org.springframework.samples.petclinic.web;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Locale;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.expression.ParseException;
import org.springframework.samples.petclinic.datatypes.Phone;
import org.springframework.samples.petclinic.datatypes.PhoneFormatter;

@ExtendWith(MockitoExtension.class)
class PhoneFormatterTests {
	private PhoneFormatter phoneFormatter;

	@BeforeEach
	void setup() {
		phoneFormatter = new PhoneFormatter();
	}

	@Test
	void testPrint() {
		Phone phone = new Phone(34, null, "600 000 000");
		String parsedPhone = phoneFormatter.print(phone, Locale.ENGLISH);
		assertEquals("+34 600 000 000", parsedPhone);
	}

	@ParameterizedTest
	@ValueSource(strings = { "+1 (123) 456-7890", "+1 456-7890", "+34 (000) 955000000" })
	void shouldParse(String value) throws ParseException {
		Phone phone = phoneFormatter.parse(value, Locale.ENGLISH);
		assertEquals(value, phone.toString());
	}

	@ParameterizedTest
	@ValueSource(strings = { "+1 () 456-7890", "+1 456 000 0 0 0 0 090", "+3400 (000) 955000000" })
	void shouldThrowParseException(String value) throws ParseException {
		Assertions.assertThrows(ParseException.class, () -> {
			phoneFormatter.parse(value, Locale.ENGLISH);
		});
	}
}