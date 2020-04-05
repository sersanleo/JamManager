package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jams/{jamId}/teams/{teamId}/marks")
public class MarkController {

	private static final String VIEWS_CREATE_OR_UPDATE_FORM = "marks/createOrUpdateForm";

	@GetMapping
	public String crearTeam(final ModelMap modelMap, final Jam jam) throws Exception {
		Mark mark = null;
		if (mark == null) {
			mark = new Mark();
		}

		modelMap.addAttribute("mark", mark);
		return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping
	public String salvarTeam(@Valid final Mark mark, final BindingResult result, final ModelMap modelMap)
			throws Exception {
		return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
	}
}
