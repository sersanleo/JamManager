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

import java.util.Collection;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Juergen Hoeller
 * @author Mark Fisher
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
public class JamController {

	private static final String	VIEWS_JAM_CREATE_OR_UPDATE_FORM	= "jams/createOrUpdateForm";

	private final JamService	jamService;


	@Autowired
	public JamController(final JamService clinicService) {
		this.jamService = clinicService;
	}

	@InitBinder
	public void setAllowedFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/jams/new")
	public String initCreationForm(final Map<String, Object> model) {
		Jam jam = new Jam();
		model.put("jam", jam);
		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/jams/new")
	public String processCreationForm(@Valid final Jam jam, final BindingResult result) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			this.jamService.saveJam(jam);

			return "redirect:/jams/" + jam.getId();
		}
	}

	@GetMapping(value = {
		"/jams"
	})
	public String showVetList(final Map<String, Object> model) {
		System.out.println(this.jamService.findJams());
		model.put("jams", this.jamService.findJams());
		return "jams/jamList";
	}

	@GetMapping(value = {
		"/jams.xml"
	})
	public @ResponseBody Collection<Jam> showResourcesVetList() {
		return this.jamService.findJams();
	}

}
