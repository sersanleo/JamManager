
package org.springframework.samples.petclinic.web;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
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

@Controller
@RequestMapping("/jams/{jamId}/teams")
public class TeamController {

	private static final String VIEWS_TEAM_CREATE_OR_UPDATE_FORM = "teams/createOrUpdateForm";

	@Autowired
	private TeamService teamService;
	@Autowired
	private UserService userService;
	@Autowired
	private JamService jamService;

	@InitBinder("team")
	public void addTeamValidator(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new TeamValidator());
		dataBinder.setDisallowedFields("id", "jam", "creationDate");
	}
	@ModelAttribute(name = "jam", binding = false)
	public Jam cargarJam(@PathVariable("jamId") final int jamId) {
		return this.jamService.findJamById(jamId);
	}

	@GetMapping("/{teamId}")
	public String mostrarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) {
		modelMap.addAttribute("team", this.teamService.findTeamById(teamId));
		modelMap.addAttribute("isMember",
				this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername()));

		return "teams/teamDetails";
	}

	@GetMapping("/new")
	public String crearTeam(final ModelMap modelMap, @PathVariable("jamId") final int jamId) throws Exception {
		if (this.teamService.findIsMemberOfTeamByJamIdAndUsername(jamId, UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		modelMap.addAttribute("team", new Team());

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String salvarTeam(final Jam jam, @Valid final Team team, final BindingResult result,
			final ModelMap modelMap) throws Exception {
		if (this.teamService.findIsMemberOfTeamByJamIdAndUsername(jam.getId(), UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {
			User member = new User();
			member.setUsername(UserUtils.getCurrentUsername());
			Set<User> members = new HashSet<User>();
			members.add(member);
			team.setMembers(members);

			team.setJam(jam);

			this.teamService.saveTeam(team);

			return "redirect:/jams/{jamId}";
		}
	}

	@GetMapping("/{teamId}/edit")
	public String editarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) throws Exception {
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		modelMap.addAttribute("team", this.teamService.findTeamById(teamId));

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/{teamId}/edit")
	public String salvarCambiosTeam(@Valid final Team team, final BindingResult result,
			@PathVariable("teamId") final int teamId, final ModelMap modelMap) throws Exception {
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {
			Team teamToUpdate = this.teamService.findTeamById(teamId);
			BeanUtils.copyProperties(teamToUpdate, team, "name");
			this.teamService.saveTeam(team);

			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}
	
	@GetMapping(value = "/{teamId}/{userId}/delete")
	public String initDeleteForm(@PathVariable("teamId") int teamId, @PathVariable("userId") String userId, ModelMap model) {
		User user= this.userService.findOnlyByUsername(userId);
		Team team = this.teamService.findTeamById(teamId);
		team.getMembers().remove(user);
		if (team.getMembers().size() == 0) {
			this.teamService.deleteTeam(team);
			return "redirect:/jams/{jamId}";
		} else {
			this.teamService.saveTeam(team);
		return "redirect:/jams/{jamId}";
		}
	}
}
