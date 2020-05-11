package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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
class JamControllerE2ETests {

	private static final int TEST_INSCRIPTION_JAM_ID = 1;
	private static final int TEST_CANCELLED_JAM_ID = 6;
	private static final int TEST_RATING_JAM_READY_TO_PUBLISH_ID = 4;
	private static final int TEST_RATING_JAM_NEEDS_SAME_ID = 9;
	private static final int TEST_RATING_JAM_AT_LEAST_ONE_ID = 8;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm");

	@Autowired
	private MockMvc mockMvc;

	private static final LocalDateTime futureDateTime(final int daysOffset) {
		return LocalDateTime.now().plusDays(daysOffset);
	}

	private static final String futureDateTimeToString(final int daysOffset) {
		return DATETIME_FORMATTER.format(futureDateTime(daysOffset));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testListJams() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/jamList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jams"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(ints = { TEST_INSCRIPTION_JAM_ID, TEST_CANCELLED_JAM_ID })
	@ParameterizedTest
	void testSuccessfulShowJam(final int jamId) throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}", jamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/jamDetails"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedShowJamNonExistent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}", TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testInitJamCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testSuccesfulJamCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedJamCreationNameError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "name"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedJamCreationDescriptionError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(strings = { "-1", "0", "6", "aegg" })
	@ParameterizedTest
	void testFailedJamCreationDifficultyError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", value)
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "difficulty"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(ints = { -5, -1, 8, 10 })
	@ParameterizedTest
	void testFailedJamCreationInscriptionDeadlineError(final int offset) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(offset))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "inscriptionDeadline"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(strings = { "-1", "0" })
	@ParameterizedTest
	void testFailedJamCreationMaxTeamSizeError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", value)
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "maxTeamSize"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(strings = { "-1", "0", "1", "3" })
	@ParameterizedTest
	void testFailedJamCreationMinTeamsError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", value)
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "minTeams"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@ValueSource(strings = { "-1", "0", "1" })
	@ParameterizedTest
	void testFailedJamCreationMaxTeamsError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", value)
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "maxTeams"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedJamCreationStartError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(-1))
						.param("end", futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "start"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedJamCreationEndError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", futureDateTimeToString(7))
						.param("end", futureDateTimeToString(-1)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "end"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testSuccessfulInitJamUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedInitJamUpdateFormNotInscriptionJam() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedInitJamUpdateFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testSuccessfulDeleteJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/delete", TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedDeleteJamNotInscriptionJam() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "jamOrganizator1", authorities = { "jamOrganizator" })
	@Test
	void testFailedDeleteJamNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testSuccesfulShowPublishResults() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/jams/{jamId}/publish", TEST_RATING_JAM_READY_TO_PUBLISH_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@ValueSource(ints = { TEST_INSCRIPTION_JAM_ID, TEST_CANCELLED_JAM_ID })
	@ParameterizedTest
	void testFailedShowPublishResultsNotRating(int jamId) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/jams/{jamId}/publish", jamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testSuccesfulPublishResults() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_READY_TO_PUBLISH_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@ValueSource(ints = { TEST_INSCRIPTION_JAM_ID, TEST_CANCELLED_JAM_ID })
	@ParameterizedTest
	void testFailedPublishResultsNotRating(int jamId) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", jamId)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testFailedPublishResultsAtLeastOneMark() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_AT_LEAST_ONE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "winner.id"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("jam", "winner.id", "atLeast1"))
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testFailedPublishResultsNeedsSameNumberOfMarks() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_NEEDS_SAME_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "winner.id"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("jam", "winner.id",
						"sameNumberOfMarks"))
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"));
	}
}