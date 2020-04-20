
package org.springframework.samples.petclinic.web;

import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamStatus;
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
	@Autowired
	private InvitationService invitationService;

	@InitBinder("team")
	public void initTeamBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id", "jam", "creationDate");
	}

	@ModelAttribute(name = "jam", binding = false)
	public Jam cargarJam(@PathVariable("jamId") final int jamId) {
		return this.jamService.findJamById(jamId);
	}

	@GetMapping("/{teamId}")
	public String mostrarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) throws Exception {
		Team team = this.teamService.findTeamById(teamId);

		modelMap.addAttribute("team", team);
		modelMap.addAttribute("isMember",
				this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername()));

		return "teams/teamDetails";
	}

	@GetMapping("/new")
	public String crearTeam(final ModelMap modelMap, final Jam jam) throws Exception {
		if (jam.getStatus() != JamStatus.INSCRIPTION
				|| this.teamService.findIsMemberOfTeamByJamIdAndUsername(jam.getId(), UserUtils.getCurrentUsername()) || jam.getIsFull()) {
			throw new Exception();
		}

		modelMap.addAttribute("team", new Team());

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String salvarTeam(@Valid final Team team, final BindingResult result, final Jam jam, final ModelMap modelMap)
			throws Exception {
		String username = UserUtils.getCurrentUsername();
		if (jam.getStatus() != JamStatus.INSCRIPTION
				|| this.teamService.findIsMemberOfTeamByJamIdAndUsername(jam.getId(), username) || jam.getIsFull()) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {

			User member = new User();
			member.setUsername(username);
			Set<User> members = new HashSet<User>();
			members.add(member);
			team.setMembers(members);

			team.setJam(jam);

			this.teamService.saveTeam(team);

			this.invitationService.deleteAllPendingInvitationsByJamIdAndUsername(jam.getId(), username);

			return "redirect:/jams/{jamId}/teams/" + team.getId();
		}
	}

	@GetMapping("/{teamId}/edit")
	public String editarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) throws Exception {
		Team team = this.teamService.findTeamById(teamId);

		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		modelMap.addAttribute("team", team);

		return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/{teamId}/edit")
	public String salvarCambiosTeam(@Valid final Team team, final BindingResult result,
			@PathVariable("teamId") final int teamId, final ModelMap modelMap) throws Exception {
		Team teamToUpdate = this.teamService.findTeamById(teamId);

		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(teamToUpdate, team, "name");
			this.teamService.saveTeam(team);

			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}

	@GetMapping(value = "/{teamId}/members/{username}/delete")
	public String initDeleteMemberForm(@PathVariable("teamId") final int teamId,
			@PathVariable("username") final String username, final ModelMap model) throws Exception {
		Team team = this.teamService.findTeamById(teamId);
		String currentUsername = UserUtils.getCurrentUsername();

		if (team.getJam().getStatus() != JamStatus.INSCRIPTION
				|| !this.teamService.findIsMemberOfTeamByTeamIdAndUsername(team.getId(),
						currentUsername)
				|| !this.teamService.findIsMemberOfTeamByTeamIdAndUsername(team.getId(),
						username)) {
			throw new Exception();
		}

		User user = this.userService.findByUsername(username);

		team.getMembers().remove(user);

		this.teamService.saveTeam(team);

		if (currentUsername.equals(username)) {
			return "redirect:/jams/{jamId}";
		} else {
			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}
}
