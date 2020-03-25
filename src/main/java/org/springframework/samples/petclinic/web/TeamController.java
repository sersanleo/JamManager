package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Jams;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.Teams;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/teams")
public class TeamController {
	
	private static final String	VIEWS_TEAM_CREATE_OR_UPDATE_FORM	= "teams/createOrUpdateForm";
	
	
		@Autowired
		private TeamService teamService;
	
		@InitBinder("team")
		public void addTeamValidator(final WebDataBinder dataBinder) {
			dataBinder.addValidators(new TeamValidator());
		}

		@GetMapping()
		public String listarJams(final ModelMap modelMap) {
			modelMap.addAttribute("teams", this.teamService.findTeams());

			return "teams/teamList";
		}

		@GetMapping("/teams.xml")
		public @ResponseBody Teams listarTeamsXml() {
			Teams teams = new Teams();

			teams.getTeamList().addAll(this.teamService.findTeams());

			return teams;
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

		
//		// AQUI NO SE MU BIEN K PONER PA LOS TEAM
//		
//		@PostMapping("/new")
//		public String salvarTeam(@Valid final Team team, final BindingResult result, final ModelMap modelMap) {
//			if (result.hasErrors()) {
//				return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
//			} else {
//				User creator = new User();
//				creator.setUsername(UserUtils.getCurrentUsername());
//				team.setCreator(creator);
//				
//				this.teamService.saveTeam(team);
//
//				return "redirect:/teams/" + team.getId();
//			}
//		}

		@GetMapping("{teamId}/edit")
		public String editarTeam(@PathVariable("teamId") final int teamId, final ModelMap modelMap) {
			modelMap.addAttribute("team", this.teamService.findTeamById(teamId));

			return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
		}

		@PostMapping("{teamId}/edit")
		public String salvarCambiosTeam(@Valid final Team team, final BindingResult result, @PathVariable("teamId") final int teamId, final ModelMap modelMap) {
			if (result.hasErrors()) {
				return TeamController.VIEWS_TEAM_CREATE_OR_UPDATE_FORM;
			} else {
				Team teamToUpdate = this.teamService.findTeamById(teamId);
				BeanUtils.copyProperties(team, teamToUpdate, "id", "rated", "creator");
				this.teamService.saveTeam(teamToUpdate);

				return "redirect:/teams/{tamId}";
			}
		}
	}