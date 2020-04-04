
package org.springframework.samples.petclinic.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.samples.petclinic.datatypes.PhoneFormatter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class FormatterRegistrar implements WebMvcConfigurer {

	@Override
	public void addFormatters(final FormatterRegistry registry) {
		PhoneFormatter phoneFormatter;

		phoneFormatter = new PhoneFormatter();
		registry.addFormatter(phoneFormatter);
	}

}
