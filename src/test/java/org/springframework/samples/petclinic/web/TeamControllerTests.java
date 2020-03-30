
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.InvitationService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.samples.petclinic.service.UserService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(value = TeamController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
public class TeamControllerTests {
	private static final int TEST_INSCRIPTION_JAM_ID = 1;
	private static final int TEST_CANCELLED_JAM_ID = 2;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;

	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;

	@MockBean
	private InvitationService invitationService;

	@MockBean
	private UserService userService;

	@MockBean
	private TeamService teamService;

	@MockBean
	private JamService jamService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	private void beforeEach() {
		Jam inscriptionJam = new Jam();
		inscriptionJam.setId(TeamControllerTests.TEST_INSCRIPTION_JAM_ID);
		inscriptionJam.setName("Inscription Jam");
		inscriptionJam.setDescription("This is a test Jam.");
		inscriptionJam.setDifficulty(5);
		inscriptionJam.setInscriptionDeadline(LocalDateTime.now().plusDays(2));
		inscriptionJam.setMaxTeamSize(5);
		inscriptionJam.setMinTeams(5);
		inscriptionJam.setMaxTeams(12);
		inscriptionJam.setStart(LocalDateTime.now().plusDays(3));
		inscriptionJam.setEnd(LocalDateTime.now().plusDays(4));
		inscriptionJam.setJamResources(new HashSet());

		BDDMockito.given(this.jamService.findJamById(TeamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.willReturn(inscriptionJam);

		Team team1 = new Team();
		team1.setId(TeamControllerTests.TEST_TEAM_ID);
		team1.setJam(inscriptionJam);
		team1.setName("team1");
		team1.setMembers(new HashSet());

		BDDMockito.given(this.teamService.findTeamById(TeamControllerTests.TEST_TEAM_ID))
				.willReturn(team1);

		Jam cancelledJam = new Jam();
		cancelledJam.setId(TeamControllerTests.TEST_CANCELLED_JAM_ID);
		cancelledJam.setName("Cancelled Jam");
		cancelledJam.setDescription("This is a test Jam.");
		cancelledJam.setDifficulty(5);
		cancelledJam.setInscriptionDeadline(LocalDateTime.now().minusDays(2));
		cancelledJam.setMaxTeamSize(5);
		cancelledJam.setMinTeams(5);
		cancelledJam.setMaxTeams(12);
		cancelledJam.setStart(LocalDateTime.now().plusDays(3));
		cancelledJam.setEnd(LocalDateTime.now().plusDays(4));
		cancelledJam.setJamResources(new HashSet());

		BDDMockito.given(this.jamService.findJamById(TeamControllerTests.TEST_CANCELLED_JAM_ID))
				.willReturn(cancelledJam);
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulShowTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}",
						TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
						TeamControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/teamDetails"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedShowTeamNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}",
						TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
						TeamControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulInitCreationForm() throws Exception {
		BDDMockito
				.given(this.teamService
						.findIsMemberOfTeamByJamIdAndUsername(TeamControllerTests.TEST_INSCRIPTION_JAM_ID, "spring"))
				.willReturn(false);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitCreationFormUserAlreadyParticipating() throws Exception {
		BDDMockito
				.given(this.teamService
						.findIsMemberOfTeamByJamIdAndUsername(TeamControllerTests.TEST_INSCRIPTION_JAM_ID, "spring"))
				.willReturn(true);

		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitCreationFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerTests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitCreationFormNotInscriptionJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerTests.TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulTeamCreation() throws Exception {
		BDDMockito
				.given(this.teamService
						.findIsMemberOfTeamByJamIdAndUsername(TeamControllerTests.TEST_INSCRIPTION_JAM_ID, "spring"))
				.willReturn(false);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedTeamCreationNonExistentJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerTests.TEST_NONEXISTENT_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedTeamCreationNotInscriptionJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerTests.TEST_CANCELLED_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedTeamCreationUserAlreadyParticipating() throws Exception {
		BDDMockito
				.given(this.teamService
						.findIsMemberOfTeamByJamIdAndUsername(TeamControllerTests.TEST_INSCRIPTION_JAM_ID, "spring"))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
