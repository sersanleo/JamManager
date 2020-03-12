package org.springframework.samples.petclinic.web;

import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Jams;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/teams ")
public class TeamController {
	
		@Autowired
		private TeamService teamService;
	
		@GetMapping()
		public String listTeams(ModelMap modelMap) {
			String vista = “teams/listTeams”;
			Iterable<Team> teams = teamService.findAll();
			modelMap.addAttribute(“teams”, teams);
			return vista;
		}

		
		
		
		@GetMapping(value = {
			"/jams"
		})
		public String showJamList(final Map<String, Object> model) {
			model.put("jams", this.jamService.findJams());

			return "jams/jamList";
		}

		@GetMapping(value = {
			"/jams/jams.xml"
		})
		public @ResponseBody Jams showResourcesJamList() {
			Jams jams = new Jams();

			jams.getJamList().addAll(this.jamService.findJams());

			return jams;
		}

		@GetMapping(value = "/jams/new")
		public String initCreationForm(final Map<String, Object> model) {
			Jam jam = new Jam();

			model.put("jam", jam);

			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		}

		@PostMapping(value = "/jams/new")
		public String processCreationForm(@Valid final Jam jam, final BindingResult result, final ModelMap model) {
			if (result.hasErrors()) {
				return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
			} else {
				SecurityContext context = SecurityContextHolder.getContext();
				org.springframework.security.core.userdetails.User user = (User)context.getAuthentication().getPrincipal();
				org.springframework.samples.petclinic.model.User creator = new org.springframework.samples.petclinic.model.User();
				creator.setUsername(user.getUsername());
				jam.setCreator(creator);
				this.jamService.saveJam(jam);

				return "redirect:/jams/" + jam.getId();
			}
		}

		@GetMapping("/jams/{jamId}")
		public ModelAndView showJam(@PathVariable("jamId") final int jamId) {
			ModelAndView mav = new ModelAndView("jams/jamDetails");

			mav.addObject(this.jamService.findJamById(jamId));

			return mav;
		}

		@GetMapping(value = "/jams/{jamId}/edit")
		public String initUpdateJamForm(@PathVariable("jamId") final int jamId, final Model model) {
			Jam jam = this.jamService.findJamById(jamId);

			model.addAttribute(jam);

			return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
		}

		@PostMapping(value = "/jams/{jamId}/edit")
		public String processUpdateOwnerForm(@Valid final Jam jam, final BindingResult result, @PathVariable("jamId") final int jamId) {
			if (result.hasErrors()) {
				return JamController.VIEWS_JAM_CREATE_OR_UPDATE_FORM;
			} else {
				jam.setId(jamId);
				this.jamService.saveJam(jam);

				return "redirect:/jams/{jamId}";
			}
		}	

}
