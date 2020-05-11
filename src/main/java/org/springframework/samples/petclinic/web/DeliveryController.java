package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Delivery;
import org.springframework.samples.petclinic.model.Invitation;
import org.springframework.samples.petclinic.model.InvitationStatus;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.AuthoritiesService;
import org.springframework.samples.petclinic.service.DeliveryService;
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
public class DeliveryController {

	@Autowired
	private DeliveryService deliveryService;
	@Autowired
	private TeamService teamService;

	@InitBinder("delivery")
	public void setInvitationBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping("/jams/{jamId}/teams/{teamId}/deliveries/new")
	public String initCreationForm(@PathVariable("teamId") final int teamId, final Map<String, Object> model)
			throws Exception {
		Team team = this.teamService.findTeamById(teamId);
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.IN_PROGRESS) {
			throw new Exception();
		}

		model.put("delivery", new Delivery());
		return "deliveries/createForm";
	}

	@PostMapping(value = "/jams/{jamId}/teams/{teamId}/deliveries/new")
	public String processCreationForm(@Valid final Delivery delivery, final BindingResult result,
			@PathVariable("teamId") final int teamId) throws Exception {
		Team team = this.teamService.findTeamById(teamId);
		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(teamId, UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.IN_PROGRESS) {
			throw new Exception();
		}


		if (result.hasErrors()) {
			return "deliveries/createForm";
		} else {
			delivery.setTeam(team);;
			this.deliveryService.saveDelivery(delivery);;
			return "redirect:/jams/{jamId}/teams/{teamId}";
		}
	}

	@GetMapping(value = "/jams/{jamId}/teams/{teamId}/deliveries/{deliveryId}/delete")
	public String initDeleteForm(@PathVariable("deliveryId") final int deliveryId, final ModelMap model)
			throws Exception {
		Delivery delivery = this.deliveryService.findDeliveryById(deliveryId);
		Team team = delivery.getTeam();

		if (!this.teamService.findIsMemberOfTeamByTeamIdAndUsername(team.getId(), UserUtils.getCurrentUsername())
				|| team.getJam().getStatus() != JamStatus.IN_PROGRESS) {
			throw new Exception();
		}
		
		team.deleteDelivery(delivery);
		this.deliveryService.deleteDelivery(delivery);
		
		return "redirect:/jams/{jamId}/teams/{teamId}";
	}


}
