package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Jams;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.util.UserUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/invitations")
public class InvitationController {

	private static final String	VIEWS_INVITATION_CREATE_OR_UPDATE_FORM	= "invitations/createOrUpdateForm";

	@Autowired
	private InvitationService			invitationService;

	@Autowired
	public InvitationController(final InvitationService invitationService) {
		this.invitationService = invitationService;
	}

	@InitBinder
	public void setAlloweFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

}
