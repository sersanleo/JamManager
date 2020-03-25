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

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Jams;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/jams")
public class JamController {

	private static final String	VIEWS_JAM_CREATE_OR_UPDATE_FORM	= "jams/createOrUpdateForm";

	@Autowired
	private JamService			jamService;


	@InitBinder("jam")
	public void setJamValidator(final WebDataBinder dataBinder) {
		dataBinder.setValidator(new JamValidator());
	}

	@GetMapping()
	public String listarJams(final ModelMap modelMap) {
		modelMap.addAttribute("jams", this.jamService.findJams());

		return "jams/jamList";
	}


	@GetMapping("/{jamId}")
	public String mostrarJam(@PathVariable("jamId") final int jamId, final ModelMap modelMap) {
		modelMap.addAttribute("jam", this.jamService.findJamById(jamId));

		return "jams/jamDetails";
	}

	@GetMapping("/new")
	public String crearJam(final ModelMap modelMap) {
		modelMap.addAttribute("jam", new Jam());

		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String salvarJam(@Valid final Jam jam, final BindingResult result, final ModelMap modelMap) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			User creator = new User();
			creator.setUsername(UserUtils.getCurrentUsername());
			jam.setCreator(creator);
			this.jamService.saveJam(jam);

			return "redirect:/jams/" + jam.getId();
		}
	}

	@GetMapping("{jamId}/edit")
	public String editarJam(@PathVariable("jamId") final int jamId, final ModelMap modelMap) {
		modelMap.addAttribute("jam", this.jamService.findJamById(jamId));

		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("{jamId}/edit")
	public String salvarCambiosJam(@Valid final Jam jam, final BindingResult result, @PathVariable("jamId") final int jamId, final ModelMap modelMap) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			Jam jamToUpdate = this.jamService.findJamById(jamId);
			BeanUtils.copyProperties(jam, jamToUpdate, "id", "creator");
			this.jamService.saveJam(jamToUpdate);

			return "redirect:/jams/{jamId}";
		}
	}
}
