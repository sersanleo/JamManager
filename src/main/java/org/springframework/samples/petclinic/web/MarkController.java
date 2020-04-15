package org.springframework.samples.petclinic.web;

import java.util.NoSuchElementException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.MarkService;
import org.springframework.samples.petclinic.service.TeamService;
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
	@Autowired
	private TeamService teamService;

	@ModelAttribute("team")
	public Team cargarTeam(@PathVariable("teamId") final int teamId) {
		return this.teamService.findTeamById(teamId);
	}

	@ModelAttribute("mark")
	public Mark cargarMark(@PathVariable("teamId") final int teamId) {
		try {
			return this.markService.findByTeamIdAndJudgeUsername(teamId, UserUtils.getCurrentUsername());
		} catch (NoSuchElementException e) {
			return new Mark();
		}
	}

	@GetMapping
	public String mostrarFormulario(final ModelMap modelMap, final Mark mark, final Team team) throws Exception {
		if (team.getJam().getStatus() != JamStatus.RATING) {
			throw new Exception();
		}

		modelMap.addAttribute("mark", mark);
		return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping
	public String salvarFormulario(@Valid final Mark mark, final BindingResult result, final ModelMap modelMap,
			final Team team)
			throws Exception {
		if (team.getJam().getStatus() != JamStatus.RATING) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return MarkController.VIEWS_CREATE_OR_UPDATE_FORM;
		} else {
			User judge = new User();
			judge.setUsername(UserUtils.getCurrentUsername());
			mark.setJudge(judge);

			mark.setTeam(team);

			this.markService.saveMark(mark);

			return "redirect:/jams/{jamId}";
		}
	}
}
