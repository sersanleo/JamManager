
package org.springframework.samples.petclinic.web;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamStatus;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/jams")
public class JamController {

	private static final String VIEWS_JAM_CREATE_OR_UPDATE_FORM = "jams/createOrUpdateForm";

	@Autowired
	private JamService jamService;
	@Autowired
	private TeamService teamService;

	@InitBinder("jam")
	public void addJamValidator(final WebDataBinder dataBinder) {
		dataBinder.addValidators(new JamValidator());
		dataBinder.setDisallowedFields("id", "rated", "creator");
	}

	@GetMapping()
	public String listarJams(final ModelMap modelMap) {
		modelMap.addAttribute("jams", this.jamService.findJams());

		return "jams/jamList";
	}

	@GetMapping("/{jamId}")
	public String mostrarJam(@PathVariable("jamId") final int jamId, final ModelMap modelMap) {
		modelMap.addAttribute("jam", this.jamService.findJamById(jamId));
		modelMap.addAttribute("isFull", this.jamService.findJamById(jamId).getIsFull());
		modelMap.addAttribute("hasTeam",
				this.teamService.findIsMemberOfTeamByJamIdAndUsername(jamId, UserUtils.getCurrentUsername()));

		return "jams/jamDetails";
	}

	@GetMapping("/new")
	public String crearJam(final ModelMap modelMap) {
		modelMap.addAttribute("jam", new Jam());

		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/new")
	public String salvarJam(@Valid final Jam jam, final BindingResult result, final ModelMap modelMap) {
		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			User creator = new User();
			creator.setUsername(UserUtils.getCurrentUsername());
			jam.setCreator(creator);

			this.jamService.saveJam(jam);

			return "redirect:/jams/" + jam.getId();
		}
	}

	@GetMapping("/{jamId}/edit")
	public String editarJam(@PathVariable("jamId") final int jamId, final ModelMap modelMap) throws Exception {
		Jam jamToUpdate = this.jamService.findJamById(jamId);
		if (jamToUpdate.getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		modelMap.addAttribute("jam", jamToUpdate);
		return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
	}

	@PostMapping("/{jamId}/edit")
	public String salvarCambiosJam(@Valid final Jam jam, final BindingResult result,
			@PathVariable("jamId") final int jamId, final ModelMap modelMap) throws Exception {
		Jam jamToUpdate = this.jamService.findJamById(jamId);
		if (jamToUpdate.getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		if (result.hasErrors()) {
			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		} else {
			BeanUtils.copyProperties(jam, jamToUpdate, "id", "rated", "creator");

			this.jamService.saveJam(jamToUpdate);

			return "redirect:/jams/{jamId}";
		}
	}

	@GetMapping("/{jamId}/delete")
	public String borrarJam(@PathVariable("jamId") final int jamId, final ModelMap modelMap) throws Exception {
		Jam jam = this.jamService.findJamById(jamId);
		if (jam.getStatus() != JamStatus.INSCRIPTION) {
			throw new Exception();
		}

		this.jamService.deleteJam(jam);

		return "redirect:/jams";
	}
}
