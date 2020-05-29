
package org.springframework.samples.petclinic.web.e2e;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
@DirtiesContext
public class TeamControllerE2ETests {
	private static final int TEST_INSCRIPTION_JAM_ID = 1;
	private static final int TEST_CANCELLED_JAM_ID = 6;
	private static final int TEST_FULL_JAM_ID = 7;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;

	private static final int TEST_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;

	private static final String TEST_TEAM1_MEMBER_USERNAME = "member2";

	@Autowired
	private MockMvc mockMvc;

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testSuccesfulShowTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}",
						TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
						TeamControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/teamDetails"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedShowTeamNonExistent() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}",
						TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
						TeamControllerE2ETests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testSuccesfulInitCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInitCreationFormUserAlreadyParticipating() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitCreationFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerE2ETests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitCreationFormNotInscriptionJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerE2ETests.TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitCreationFormFullJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/new",
						TeamControllerE2ETests.TEST_FULL_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@ValueSource(strings = { "test1", "3", ";.", "hola" })
	@ParameterizedTest
	void testSuccesfulTeamCreation(final String name) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", name))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedTeamCreationNonExistentJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerE2ETests.TEST_NONEXISTENT_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedTeamCreationNotInscriptionJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerE2ETests.TEST_CANCELLED_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedTeamCreationUserAlreadyParticipating() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedTeamCreationFullJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/new",
								TeamControllerE2ETests.TEST_FULL_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testSuccessfulInitEditionForm() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("teams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("team"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedInitEditionFormNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerE2ETests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedInitEditionFormNotMember() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/edit", TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerE2ETests.TEST_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@ValueSource(strings = { "test1", "3", ";.", "hola" })
	@ParameterizedTest
	void testSuccessfulEdition(final String name) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", name))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}/teams/{teamId}"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedEditionNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerE2ETests.TEST_NONEXISTENT_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedEditionNotMember() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/teams/{teamId}/edit",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test team"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testSuccessfulMemberDeletion() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID,
								TeamControllerE2ETests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedMemberDeletionNonExistentTeam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID,
								TeamControllerE2ETests.TEST_NONEXISTENT_TEAM_ID,
								TeamControllerE2ETests.TEST_TEAM1_MEMBER_USERNAME))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedMemberDeletionNotInscriptionJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerE2ETests.TEST_CANCELLED_JAM_ID,
								3,
								"member2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member1", authorities = { "member" })
	@Test
	void testFailedMemberDeletionNotMember() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID,
								"member2"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "member2", authorities = { "member" })
	@Test
	void testFailedMemberDeletionMemberNotFound() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/teams/{teamId}/members/{username}/delete",
								TeamControllerE2ETests.TEST_INSCRIPTION_JAM_ID, TeamControllerE2ETests.TEST_TEAM_ID,
								"nonExistentUser"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

}
