
package org.springframework.samples.petclinic.web;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.model.User;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.util.Dashboard;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/dashboard")
public class DashboardController {

	@Autowired
	private JamService jamService;

	@ModelAttribute("dashboard")
	public Dashboard createDashboard() {
		Dashboard dashboard = new Dashboard();

		Collection<Jam> jams = this.jamService.findJams();

		Map<JamStatus, Long> jamsByStatus = jams.stream()
				.collect(Collectors.groupingBy(Jam::getStatus, Collectors.counting()));

		dashboard.setJamsInProgress(jamsByStatus.get(JamStatus.IN_PROGRESS));
		dashboard.setJamsInscription(jamsByStatus.get(JamStatus.INSCRIPTION));
		dashboard.setJamsPending(jamsByStatus.get(JamStatus.PENDING));
		dashboard.setJamsRating(jamsByStatus.get(JamStatus.RATING));

		Collection<Jam> activeJams = jams.stream()
				.filter(jam -> jam.getStatus() == JamStatus.IN_PROGRESS || jam.getStatus() == JamStatus.INSCRIPTION
						|| jam.getStatus() == JamStatus.PENDING || jam.getStatus() == JamStatus.RATING)
				.collect(Collectors.toList());

		dashboard.setActiveJams(activeJams);

		Map<String, Long> winners = jams.stream()
				.filter(jam -> jam.getWinner() != null).map(jam -> jam.getWinner().getMembers())
				.flatMap(Collection::stream).collect(Collectors.groupingBy(User::getUsername, Collectors.counting()));

		dashboard.setWinners(winners);

		return dashboard;
	}

	@GetMapping
	public String getDashboard(final Dashboard dashboard, ModelMap modelMap) {
		modelMap.addAttribute("dashboard", dashboard);

		return "/dashboard/dashboard";
	}
}
