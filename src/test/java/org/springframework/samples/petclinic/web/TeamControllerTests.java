
package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.samples.petclinic.configuration.SecurityConfiguration;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
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

	private static final String TEST_TEAM1_MEMBER_USERNAME = "memberUser";

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
		User team1Member = new User();
		team1Member.setUsername(TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME);
		team1.setMembers(new HashSet<User>() {
			{
				this.add(team1Member);
			}
		});

		BDDMockito.given(this.teamService.findTeamById(TeamControllerTests.TEST_TEAM_ID))
				.willReturn(team1);
		BDDMockito.given(this.userService.findByUsername(TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(team1Member);

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
	@ValueSource(strings = { "test1", "3", ";.", "hola" })
	@ParameterizedTest
	void testSuccesfulTeamCreation(final String name) throws Exception {
		BDDMockito
				.given(this.teamService
						.findIsMemberOfTeamByJamIdAndUsername(TeamControllerTests.TEST_INSCRIPTION_JAM_ID, "spring"))
				.willReturn(false);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", name))
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

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitEditionForm() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitEditionFormNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitEditionFormNotMember() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(false);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit", TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerTests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "test1", "3", ";.", "hola" })
	@ParameterizedTest
	void testSuccessfulEdition(final String name) throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", name))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}/teams/{teamId}"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedEditionNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerTests.TEST_NONEXISTENT_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedEditionNotMember() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(false);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulMemberDeletion() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID,
								TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedMemberDeletionNonExistentTeam() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerTests.TEST_NONEXISTENT_TEAM_ID,
								TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedMemberDeletionNotInscriptionJam() throws Exception {
		Jam jam = this.jamService.findJamById(TeamControllerTests.TEST_INSCRIPTION_JAM_ID);
		jam.setInscriptionDeadline(LocalDateTime.now().minusDays(1));

		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerTests.TEST_TEAM_ID,
								TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedMemberDeletionNotMember() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(false);
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(true);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID,
								TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedMemberDeletionMemberNotFound() throws Exception {
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						"spring"))
				.willReturn(true);
		BDDMockito
				.given(this.teamService.findIsMemberOfTeamByTeamIdAndUsername(TeamControllerTests.TEST_TEAM_ID,
						TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.willReturn(false);

		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerTests.TEST_INSCRIPTION_JAM_ID, TeamControllerTests.TEST_TEAM_ID,
								TeamControllerTests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
