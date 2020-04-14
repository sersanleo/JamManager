package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.MarkService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jams/{jamId}/teams/{teamId}/marks")
public class MarkController {

	private static final String VIEWS_CREATE_OR_UPDATE_FORM = "marks/createOrUpdateForm";

	@Autowired
	private MarkService markService;

	@ModelAttribute("mark")
	public Mark cargarMark(@PathVariable("teamId") final int teamId) {
		Mark mark = this.markService.findByTeamIdAndJudgeUsername(teamId, UserUtils.getCurrentUsername());
		if (mark == null) {
			mark = new Mark();
		}
		return mark;
	}

	@GetMapping
	public String mostrarFormulario(final ModelMap modelMap, final Mark mark) throws Exception {
		modelMap.addAttribute("mark", mark);
		return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping
	public String salvarFormulario(@Valid final Mark mark, final BindingResult result, final ModelMap modelMap,
			@PathVariable("teamId") final int teamId)
			throws Exception {
		if (result.hasErrors()) {
			return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			User judge = new User();
			judge.setUsername(UserUtils.getCurrentUsername());
			mark.setJudge(judge);

			Team team = new Team();
			team.setId(teamId);
			mark.setTeam(team);

			this.markService.saveMark(mark);

			return "redirect:/jams/{jamId}";
		}
	}
}
