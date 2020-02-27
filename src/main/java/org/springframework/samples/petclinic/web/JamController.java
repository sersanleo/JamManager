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
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class JamController {

	private static final String	VIEWS_JAM_CREATE_OR_UPDATE_FORM	= "jams/createOrUpdateForm";

	private final JamService	jamService;


	@Autowired
	public JamController(final JamService clinicService) {
		this.jamService = clinicService;
	}

	@InitBinder
	public void initJamBinder(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new JamValidator());
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = {
		"/jams"
	})
	public String showJamList(final Map<String, Object> model) {
		model.put("jams", this.jamService.findJams());

		return "jams/jamList";
	}

	@GetMapping(value = {
		"/jams.xml"
	})
	public @ResponseBody Collection<Jam> showResourcesVetList() {
		return this.jamService.findJams();
	}

	@GetMapping(value = "/jams/new")
	public String initCreationForm(final Map<String, Object> model) {
		Jam jam = new Jam();

		model.put("jam", jam);

		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/jams/new")
	public String processCreationForm(@Valid final Jam jam, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			this.jamService.saveJam(jam);

			return "redirect:/jams/" + jam.getId();
		}
	}

	@GetMapping("/jams/{jamId}")
	public ModelAndView showJam(@PathVariable("jamId") final int jamId) {
		ModelAndView mav = new ModelAndView("jams/jamDetails");

		mav.addObject(this.jamService.findJamById(jamId));

		return mav;
	}

	@GetMapping(value = "/jams/{jamId}/edit")
	public String initUpdateJamForm(@PathVariable("jamId") final int jamId, final Model model) {
		Jam jam = this.jamService.findJamById(jamId);

		model.addAttribute(jam);

		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/jams/{jamId}/edit")
	public String processUpdateOwnerForm(@Valid final Jam jam, final BindingResult result, @PathVariable("jamId") final int jamId) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			jam.setId(jamId);
			this.jamService.saveJam(jam);

			return "redirect:/jams/{jamId}";
		}
	}
}
