
package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = TeamController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class TeamControllerTests {

	private static final long	DAY_TO_MILLIS	= 1000 * 60 * 60 * 24;

	private static final int	TEST_TEAM_ID	= 1;

	@Autowired
	private TeamController		teamController;

	@MockBean
	private TeamService			teamService;

	@Autowired
	private MockMvc				mockMvc;


	@BeforeEach
	void setup() {
		Team team = new Team();
		team.setName("PruebaTeam");

		BDDMockito.given(this.teamService.findTeamById(TeamControllerTests.TEST_TEAM_ID)).willReturn(team);
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/teams/new")).andExpect(MockMvcResultMatchers.status().isOk()).andExpect(MockMvcResultMatchers.view().name("teams/createOrUpdateForm"))
			.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

}
