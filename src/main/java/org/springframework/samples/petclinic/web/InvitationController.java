
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.Invitations;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class InvitationController {

	private static final String VIEWS_INVITATION_TEAM = "/jams/{jamId}/teams";

	@Autowired
	private InvitationService invitationService;
	@Autowired
	private TeamService teamService;

	@Autowired
	private UserService userService;

	@Autowired
	public InvitationController(final InvitationService invitationService, final UserService userService,
			final TeamService teamService) {
		this.invitationService = invitationService;
		this.userService = userService;
		this.teamService = teamService;
	}

	@InitBinder
	public void setAlloweFields(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/invitations.xml")
	public @ResponseBody Invitations listarInvitationsXml() {
		Invitations invitations = new Invitations();
		return invitations;
	}

	@GetMapping("/invitations")
	public String listarInvitationsUser(final ModelMap modelMap) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String userName = authentication.getName();
		Collection<Invitation> invitations = this.invitationService.findInvitations().stream()
				.filter(i -> i.getTo().getUsername().equals(userName) && i.getStatus().equals(InvitationStatus.PENDING))
				.collect(Collectors.toList());
		modelMap.addAttribute("invitations", invitations);

		return "users/invitationListUser";
	}

	@GetMapping(value = "/jams/{jamId}/teams/{teamId}/invitations/new")
	public String initCreationForm(final Map<String, Object> model) {
		Invitation invitation = new Invitation();
		model.put("invitation", invitation);
		return "invitations/createForm";
	}

	@PostMapping(value = "/jams/{jamId}/teams/{teamId}/invitations/new")
	public String processCreationForm(@Valid final Invitation invitation, final BindingResult result,
			@PathVariable("teamId") final int teamId) {
		if (result.hasErrors()) {
			return "invitations/createForm";
		} else {
			// creating owner, user and authorities

			invitation.setFrom(this.teamService.findTeamById(teamId));

			this.invitationService.saveInvitation(invitation);
			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}

}
