
package org.springframework.samples.petclinic.web;


import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.service.DashboardService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/dashboard")
public class DashboardController {

//	@Autowired
//	private DashboardService dashboardService;
//	@Autowired
//	private JamService jamService;
//	@Autowired
//	private TeamService teamService;
	
	private final DashboardService dashboardService;
	private final JamService jamService;
	private final TeamService teamService;
	
	@Autowired
	public DashboardController(final DashboardService dashboardService, JamService jamService, TeamService teamService) {
		this.dashboardService = dashboardService;
		this.jamService = jamService;
		this.teamService = teamService;
	}

	@GetMapping("/general")
	public String getDashboardGeneral(Model model) {
		
		Integer jams = this.dashboardService.countJams();
		Integer teams = this.dashboardService.countTeams();
		
		return "/dashboard/dashboard";
	}
	
	
//	@GetMapping("/dashboardMonthly/{month}")
//	public String getDashboardMonthly(@PathVariable("month") int month, ModelMap model) {
//
//		Date now = new Date();
//		Calendar cal = Calendar.getInstance();
//		cal.setTime(now);
//		if (month == 0)
//			month = cal.get(Calendar.MONTH) + 1;
//		List<Jam> jams = (List<Jam>) this.dashboardService.findJamsOfMonth(month);
//
//		if (!jams.isEmpty()) {
//			model.addAttribute("JamsExists", true);
//			dashboardMonthly(jams, month, model);
//		} else {
//			model.addAttribute("JamsExists", false);
//		}
//
//		return "/dashboardMonthly";
//	}
//
//	private void dashboardMonthly(List<Jam> jams, int month, ModelMap model) {
//		// TODO Auto-generated method stub
//		
//	}
}
