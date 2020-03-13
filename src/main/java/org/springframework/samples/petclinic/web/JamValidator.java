/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.web;

import org.springframework.samples.petclinic.model.Jam;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

public class JamValidator implements Validator {

	private static final String REQUIRED = "required";


	@Override
	public void validate(final Object obj, final Errors errors) {
		Jam jam = (Jam) obj;

		if (jam.getMinTeams() > jam.getMaxTeams()) {
			String errorMessage = "The maximum number of teams must be greater than the minimum";
			errors.rejectValue("maxTeams", errorMessage, errorMessage);
		}

		if (jam.getInscriptionDeadline().isAfter(jam.getStart())) {
			String errorMessage = "The inscription deadline must be before the start of the event";
			errors.rejectValue("inscriptionDeadline", errorMessage, errorMessage);
		}

		if (jam.getStart().isAfter(jam.getEnd())) {
			String errorMessage = "The start of the event must be before the end of itself";
			errors.rejectValue("start", errorMessage, errorMessage);
		}
	}

	/**
	 * This Validator validates *just* Pet instances
	 */
	@Override
	public boolean supports(final Class<?> clazz) {
		return Jam.class.isAssignableFrom(clazz);
	}

}
