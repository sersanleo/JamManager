package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Jams;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

@Controller
public class InvitationController {

	private static final String	VIEWS_INVITATION_CREATE_OR_UPDATE_FORM	= "invitations/createOrUpdateForm";

	@Autowired
	private InvitationService			invitationService;
	@Autowired
	private TeamService					teamService;
	
	@Autowired
	private UserService		userService;
	
	
	@Autowired
	public InvitationController(final InvitationService invitationService, final UserService userService, final TeamService teamService) {
		this.invitationService = invitationService;
		this.userService = userService;
		this.teamService = teamService;
	}
	
	@ModelAttribute("team")
	public Team findTeam(@PathVariable("teamId") final int teamId) {
		return this.teamService.findTeamById(teamId);
	}

	@InitBinder("team")
	public void initTeamBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
	
	@InitBinder
	public void setAlloweFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}
/*
	@GetMapping("/teams/{teamId}/invitations")
	public String showInvitationsByTeam(final ModelMap modelMap, @PathVariable("teamId") int teamId) {
		
		Collection<Invitation> invitations = this.invitationService.findInvitations();
		invitations.stream().filter(i -> i.getFrom().getId().equals(teamId) && i.getStatus().equals(InvitationStatus.PENDING));
		modelMap.addAttribute("invitations", invitations);
		return "redirect:invitations/invitationsList";
	}
*/
//Habria que hacer lo mismo para user
	
	/*@GetMapping("/user/{userId}/invitations")
	public String showInvitationsByUser(final ModelMap modelMap, @PathVariable("userId") final int userId) {
		Collection<Invitation> invitations = this.invitationService.findInvitations();
		invitations.stream().filter(i -> i.getTo().getUsername().equals(userId) && i.getStatus().equals(InvitationStatus.PENDING));
		modelMap.addAttribute("invitations", invitations);
		return "redirect:invitations/invitationsList";
	}*/
	

}
