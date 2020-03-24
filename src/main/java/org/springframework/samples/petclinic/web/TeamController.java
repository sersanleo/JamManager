package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Booking;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.Teams;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/jams/{jamId}/teams")
public class TeamController {

	private static final String VIEWS_TEAM_CREATE_OR_UPDATE_FORM = "teams/createOrUpdateForm";

	@Autowired
	private TeamService teamService;

	@Autowired
	private JamService jamService;

	@InitBinder("team")
	public void addTeamValidator(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new TeamValidator());
		dataBinder.setDisallowedFields("id", "jam", "creationDate");
	}

	@ModelAttribute("team")
	public Team cargarTeam(@PathVariable("jamId") final int jamId) {
		Team team = new Team();

		Jam jam = this.jamService.findJamById(jamId);
		team.setJam(jam);
		team.setCreationDate(LocalDateTime.now());

		return team;
	}

	@GetMapping("/{teamId}")
	public String mostrarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) {
		modelMap.addAttribute("team", this.teamService.findTeamById(teamId));

		return "teams/teamDetails";
	}

	@GetMapping("/new")
	public String crearTeam(final ModelMap modelMap) {
		modelMap.addAttribute("team", new Team());

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String salvarTeam(@Valid final Team team, final Jam jam, final BindingResult result,
			final ModelMap modelMap) {
		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {
			User member = new User();
			member.setUsername(UserUtils.getCurrentUsername());

			Set<User> members = new HashSet();
			members.add(member);

			team.setMembers(members);

			this.teamService.saveTeam(team);

			return "redirect:/jams/{jamId}";
		}
	}

	@GetMapping("{teamId}/edit")
	public String editarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) {
		modelMap.addAttribute("team", this.teamService.findTeamById(teamId));

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("{teamId}/edit")
	public String salvarCambiosTeam(@Valid final Team team, final BindingResult result,
			@PathVariable("teamId") final int teamId, final ModelMap modelMap) {
		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {
			Team teamToUpdate = this.teamService.findTeamById(teamId);
			BeanUtils.copyProperties(team, teamToUpdate, "id", "jam", "creationDate");
			this.teamService.saveTeam(teamToUpdate);

			return "redirect:/teams/{teamId}";
		}
	}
}