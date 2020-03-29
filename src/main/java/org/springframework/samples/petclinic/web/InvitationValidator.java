package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class InvitationValidator implements Validator {

	private static final String REQUIRED = "required";

	@Override
	public void validate(final Object obj, final Errors errors) {
		Invitation invitation = (Invitation) obj;
		
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Invitation.class.isAssignableFrom(clazz);
	}

}
