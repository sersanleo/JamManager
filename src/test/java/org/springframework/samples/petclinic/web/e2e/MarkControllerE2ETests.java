package org.springframework.samples.petclinic.web.e2e;

import java.time.LocalDateTime;

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
class MarkControllerE2ETests {

	private static final int TEST_RATING_JAM_ID = 4;
	private static final int TEST_RATING_TEAM_ID1 = 6;
	private static final int TEST_RATING_TEAM_ID2 = 7;
	private static final int TEST_NOTRATING_JAM_ID = 1;
	private static final int TEST_NOTRATING_TEAM_ID = 1;
	private static final int TEST_NONEXISTENT_TEAM_ID = 100;

	@Autowired
	private MockMvc mockMvc;

	private static final LocalDateTime futureDateTime(final int daysOffset) {
		return LocalDateTime.now().plusDays(daysOffset);
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@ValueSource(ints = { MarkControllerE2ETests.TEST_RATING_TEAM_ID1, MarkControllerE2ETests.TEST_RATING_TEAM_ID2 })
	@ParameterizedTest
	void testShowFormSuccessful(final int teamId) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerE2ETests.TEST_RATING_JAM_ID, teamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("marks/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("mark"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testShowFormFailedNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerE2ETests.TEST_RATING_JAM_ID, MarkControllerE2ETests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testShowFormFailedNotRatingJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerE2ETests.TEST_RATING_JAM_ID, MarkControllerE2ETests.TEST_NOTRATING_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testProcessFormSuccessful() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerE2ETests.TEST_RATING_JAM_ID,
										MarkControllerE2ETests.TEST_RATING_TEAM_ID1)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@ValueSource(strings = { "-1", "11", "-5", "20", "0,2", "notANumber" })
	@ParameterizedTest
	void testProcessFormFailedWrongMark(final String mark) throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerE2ETests.TEST_RATING_JAM_ID,
										MarkControllerE2ETests.TEST_RATING_TEAM_ID1)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", mark)
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("mark"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("mark", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mark", "value"))
				.andExpect(MockMvcResultMatchers.view().name("marks/createOrUpdateForm"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testProcessFormFailedNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerE2ETests.TEST_RATING_JAM_ID,
										MarkControllerE2ETests.TEST_NONEXISTENT_TEAM_ID)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username = "judge1", authorities = { "judge" })
	@Test
	void testProcessFormFailedNotRatingJam() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerE2ETests.TEST_RATING_JAM_ID,
										MarkControllerE2ETests.TEST_NOTRATING_TEAM_ID)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
