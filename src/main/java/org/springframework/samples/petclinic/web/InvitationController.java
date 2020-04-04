
package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class InvitationController {

	@Autowired
	private InvitationService invitationService;
	@Autowired
	private TeamService teamService;
	@Autowired
	private UserService userService;
	@Autowired
	private AuthoritiesService authoritiesService;

	@InitBinder("invitation")
	public void setInvitationBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/invitations")
	public String listarInvitationsUser(final ModelMap modelMap) {
		modelMap.addAttribute("invitations",
				this.invitationService.findPendingInvitationsByUsername(UserUtils.getCurrentUsername()));

		return "invitations/invitationList";
	}

	@GetMapping(value = "/jams/{jamId}/teams/{teamId}/invitations/new")
	public String initCreationForm(@PathVariable("teamId") final int teamId, final Map<String, Object> model)
			throws Exception {
		Team team = this.teamService.findTeamById(teamId);
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		model.put("invitation", new Invitation());
		return "invitations/createForm";
	}

	@PostMapping(value = "/jams/{jamId}/teams/{teamId}/invitations/new")
	public String processCreationForm(@Valid final Invitation invitation, final BindingResult result,
			@PathVariable("teamId") final int teamId) throws Exception {
		Team team = this.teamService.findTeamById(teamId);
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		String toUsername = invitation.getTo().getUsername();
		User toUser = this.userService.findByUsername(toUsername);

		if (toUser == null) {
			result.rejectValue("to.username", "wrongUser", "This user doesn't exist");
		} else if (this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, toUsername)) {
			result.rejectValue("to.username", "isMember", "This user is a member of the team");
		} else if (this.teamService.findIsMemberOfTeamByJamIdAndUsername(team.getJam().getId(), toUsername)) {
			result.rejectValue("to.username", "isParticipating", "This user is already participating in this jam");
		} else if (this.invitationService.findHasPendingInvitationsByTeamIdAndUsername(teamId, toUsername)) {
			result.rejectValue("to.username", "pendingInvitation", "There's a pending invitation yet");
		} else if (!this.authoritiesService.findHasAuthorityByUsername(toUsername, "member")) {
			result.rejectValue("to.username", "notMember", "This is not a member");
		}

		if (result.hasErrors()) {
			return "invitations/createForm";
		} else {
			invitation.setFrom(team);
			this.invitationService.saveInvitation(invitation);

			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}

	@GetMapping(value = "/jams/{jamId}/teams/{teamId}/invitations/{invitationId}/delete")
	public String initDeleteForm(@PathVariable("invitationId") final int invitationId, final ModelMap model)
			throws Exception {
		Invitation invitation = this.invitationService.findInvitationById(invitationId);
		Team team = invitation.getFrom();

		if (invitation == null || invitation.getStatus() != InvitationStatus.PENDING
				|| !this.teamService.findIsMemberOfTeamByTeamIdAndUsername(team.getId(), UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		this.invitationService.deleteInvitation(invitation);

		return "redirect:/jams/{jamId}/teams/{teamId}";
	}

	@GetMapping(value = "/invitations/{invitationId}/accept")
	public String processAccept(@PathVariable("invitationId") final int invitationId, final ModelMap model)
			throws Exception {
		Invitation invitation = this.invitationService.findInvitationById(invitationId);
		String currentUsername = UserUtils.getCurrentUsername();

		if (invitation == null || invitation.getStatus() != InvitationStatus.PENDING
				|| !invitation.getTo().getUsername().equals(currentUsername)
				|| invitation.getFrom().getJam().getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		invitation.setStatus(InvitationStatus.ACCEPTED);
		this.invitationService.saveInvitation(invitation);

		// Borrar todas las solicitudes pendientes que ya no podrá aceptar
		this.invitationService.deleteAllPendingInvitationsByJamIdAndUsername(invitation.getFrom().getJam().getId(),
				invitation.getTo().getUsername());

		// Actualizar miembros del equipo
		Team team = invitation.getFrom();
		User usuario = new User();
		usuario.setUsername(currentUsername);
		team.getMembers().add(usuario);
		this.teamService.saveTeam(team);

		return "redirect:/jams/" + invitation.getFrom().getJam().getId() + "/teams/" + invitation.getFrom().getId();
	}

	@GetMapping(value = "/invitations/{invitationId}/reject")
	public String processReject(@PathVariable("invitationId") final int invitationId, final ModelMap model)
			throws Exception {
		Invitation invitation = this.invitationService.findInvitationById(invitationId);
		String currentUsername = UserUtils.getCurrentUsername();

		if (invitation == null || invitation.getStatus() != InvitationStatus.PENDING
				|| !invitation.getTo().getUsername().equals(currentUsername)
				|| invitation.getFrom().getJam().getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		invitation.setStatus(InvitationStatus.REJECTED);
		this.invitationService.saveInvitation(invitation);
		return "redirect:/invitations";
	}

}
