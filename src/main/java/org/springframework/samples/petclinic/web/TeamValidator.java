package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;

import org.springframework.samples.petclinic.model.Team;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class TeamValidator implements Validator {

	private static final String REQUIRED = "required";
	
	@Override
	public void validate(final Object obj, final Errors errors) {
		Team team = (Team) obj;

		LocalDateTime now = LocalDateTime.now();

	
		
		// ME FALTAN LAS COSAS DE AKI
		
	}

	@Override
	public boolean supports(final Class<?> clazz) {
		return Team.class.isAssignableFrom(clazz);
	}

}