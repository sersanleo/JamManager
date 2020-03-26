package org.springframework.samples.petclinic.web;

import java.util.Collection;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.model.Pet;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.service.JamResourceService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.OwnerService;
import org.springframework.samples.petclinic.service.PetService;
import org.springframework.samples.petclinic.service.exceptions.DuplicatedPetNameException;
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
import org.springframework.web.servlet.ModelAndView;

/**
 * @author Juergen Hoeller
 * @author Ken Krebs
 * @author Arjen Poutsma
 */
@Controller
@RequestMapping("/jams/{jamId}")
public class JamResourceController {

	private static final String	VIEWS_JAM_RESOURCE_CREATE_OR_UPDATE_FORM	= "jams/createOrUpdateJamResourceForm";

	private final JamResourceService jamResourceService;
	private final JamService	jamService;


	@Autowired
	public JamResourceController(final JamResourceService jamResourceService, final JamService jamService) {
		this.jamResourceService = jamResourceService;
		this.jamService = jamService;
	}

	@ModelAttribute("jam")
	public Jam findJam(@PathVariable("jamId") final int jamId) {
		return this.jamService.findJamById(jamId);
	}


	@InitBinder("jam")
	public void initJamBinder(final WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@InitBinder
	public void setAlloweFields(WebDataBinder dataBinder) {
		dataBinder.setDisallowedFields("id");
	}

	@GetMapping(value = "/jamResources/new")
	public String initCreationForm(final Jam jam, final ModelMap model) {
		JamResource jamResource = new JamResource();
		jam.addJamResource(jamResource);
		model.put("jamResource", jamResource);
		return JamResourceController.VIEWS_JAM_RESOURCE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value = "/jamResources/new")
	public String processCreationForm(final Jam jam, @Valid final JamResource jamResource, final BindingResult result, final ModelMap model) {
		if (result.hasErrors()) {
			model.put("jamResource", jamResource);
			return JamResourceController.VIEWS_JAM_RESOURCE_CREATE_OR_UPDATE_FORM;
		} else {
			jam.addJamResource(jamResource);
			this.jamResourceService.saveJamResource(jamResource);
			return "redirect:/jams/{jamId}";
		}
	}

	@GetMapping(value ="/jamResources/{jamResourceId}/edit")
	public String editJamResource(@PathVariable("jamResourceId") int jamResourceId, ModelMap modelMap, Jam jam) {
		
		JamResource jamResource = this.jamResourceService.findJamResourceById(jamResourceId);
		modelMap.put("jamResource", jamResource);
		return JamResourceController.VIEWS_JAM_RESOURCE_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping(value ="/jamResources/{jamResourceId}/edit")
	public String updateJamResource(@Valid JamResource jamResource, BindingResult result,  @PathVariable("jamResourceId") int jamResourceId, Jam jam, ModelMap model) {
		if (result.hasErrors()) {
			model.put("jamResource", jamResource);
			return JamResourceController.VIEWS_JAM_RESOURCE_CREATE_OR_UPDATE_FORM;
		} else {
			JamResource jamResourceToUpdate = this.jamResourceService.findJamResourceById(jamResourceId);
			BeanUtils.copyProperties(jamResource, jamResourceToUpdate, "id", "jam");
			this.jamResourceService.saveJamResource(jamResourceToUpdate);
			return "redirect:/jams/{jamId}";
		}}
	
	@GetMapping(value = "/jamResources/{jamResourceId}/delete")
	public String initDeleteForm(@PathVariable("jamResourceId") int jamResourceId, ModelMap model, Jam jam) {
		JamResource jamResource = this.jamResourceService.findJamResourceById(jamResourceId);
		jam.deleteJamResource(jamResource);
		model.remove("jamResource", jamResource);
		this.jamResourceService.deleteJamResource(jamResource);
		return "redirect:/jams/{jamId}";
	}
	
}
