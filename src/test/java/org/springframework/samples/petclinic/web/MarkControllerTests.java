package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;

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
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.MarkService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = MarkController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class MarkControllerTests {

	private static final int TEST_RATING_JAM_ID = 1;
	private static final int TEST_RATING_TEAM_ID1 = 1;
	private static final int TEST_RATING_TEAM_ID2 = 2;
	private static final int TEST_NOTRATING_JAM_ID = 1;
	private static final int TEST_NOTRATING_TEAM_ID = 3;
	private static final int TEST_NONEXISTENT_TEAM_ID = 30;

	@MockBean
	private MarkService markService;
	@MockBean
	private TeamService teamService;

	@Autowired
	private MockMvc mockMvc;

	private static final LocalDateTime futureDateTime(final int daysOffset) {
		return LocalDateTime.now().plusDays(daysOffset);
	}

	@BeforeEach
	private void beforeEach() {
		// Rating Jam
		Jam ratingJam = new Jam();
		ratingJam.setId(MarkControllerTests.TEST_RATING_JAM_ID);
		ratingJam.setInscriptionDeadline(MarkControllerTests.futureDateTime(-4));
		ratingJam.setStart(MarkControllerTests.futureDateTime(-2));
		ratingJam.setEnd(MarkControllerTests.futureDateTime(-1));
		ratingJam.setMinTeams(2);

		Team ratingJamTeam1 = new Team();
		ratingJamTeam1.setId(MarkControllerTests.TEST_RATING_TEAM_ID1);
		ratingJamTeam1.setJam(ratingJam);

		Team ratingJamTeam2 = new Team();
		ratingJamTeam2.setId(MarkControllerTests.TEST_RATING_TEAM_ID2);
		ratingJamTeam2.setJam(ratingJam);

		ratingJam.setTeams(new HashSet<Team>() {
			{
				this.add(ratingJamTeam1);
				this.add(ratingJamTeam2);
			}
		});

		BDDMockito.given(this.teamService.findTeamById(MarkControllerTests.TEST_RATING_TEAM_ID1))
				.willReturn(ratingJamTeam1);
		BDDMockito.given(this.teamService.findTeamById(MarkControllerTests.TEST_RATING_TEAM_ID2))
				.willReturn(ratingJamTeam2);

		// Not a rating Jam
		Jam inscriptionJam = new Jam();
		inscriptionJam.setId(MarkControllerTests.TEST_NOTRATING_JAM_ID);
		inscriptionJam.setInscriptionDeadline(MarkControllerTests.futureDateTime(2));
		inscriptionJam.setStart(MarkControllerTests.futureDateTime(4));
		inscriptionJam.setEnd(MarkControllerTests.futureDateTime(6));
		inscriptionJam.setMinTeams(2);

		Team inscriptionJamTeam = new Team();
		inscriptionJamTeam.setId(MarkControllerTests.TEST_NOTRATING_TEAM_ID);
		inscriptionJamTeam.setJam(inscriptionJam);

		BDDMockito.given(this.teamService.findTeamById(MarkControllerTests.TEST_NOTRATING_TEAM_ID))
				.willReturn(inscriptionJamTeam);

		BDDMockito.given(
				this.markService.findByTeamIdAndJudgeUsername(MarkControllerTests.TEST_RATING_TEAM_ID1, "spring"))
				.willReturn(new Mark());
		BDDMockito.given(
				this.markService.findByTeamIdAndJudgeUsername(MarkControllerTests.TEST_RATING_TEAM_ID2, "spring"))
				.willReturn(new Mark());
		BDDMockito.given(
				this.markService.findByTeamIdAndJudgeUsername(MarkControllerTests.TEST_NOTRATING_TEAM_ID, "spring"))
				.willReturn(new Mark());

		BDDMockito.given(this.teamService.findTeamById(TEST_NONEXISTENT_TEAM_ID))
				.willThrow(NoSuchElementException.class);
	}

	@WithMockUser(value = "spring")
	@ValueSource(ints = { MarkControllerTests.TEST_RATING_TEAM_ID1, MarkControllerTests.TEST_RATING_TEAM_ID2 })
	@ParameterizedTest
	void testShowFormSuccessful(final int teamId) throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerTests.TEST_RATING_JAM_ID, teamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("marks/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("mark"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowFormFailedNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerTests.TEST_RATING_JAM_ID, MarkControllerTests.TEST_NONEXISTENT_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testShowFormFailedNotRatingJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/teams/{teamId}/marks",
						MarkControllerTests.TEST_RATING_JAM_ID, MarkControllerTests.TEST_NOTRATING_TEAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFormSuccessful() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerTests.TEST_RATING_JAM_ID,
										MarkControllerTests.TEST_RATING_TEAM_ID1)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "-1", "11", "-5", "20", "0,2", "notANumber" })
	@ParameterizedTest
	void testProcessFormFailedWrongMark(final String mark) throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerTests.TEST_RATING_JAM_ID,
										MarkControllerTests.TEST_RATING_TEAM_ID1)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", mark)
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("mark"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("mark", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("mark", "value"))
				.andExpect(MockMvcResultMatchers.view().name("marks/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFormFailedNonExistentTeam() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerTests.TEST_RATING_JAM_ID,
										MarkControllerTests.TEST_NONEXISTENT_TEAM_ID)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testProcessFormFailedNotRatingJam() throws Exception {
		this.mockMvc
				.perform(
						MockMvcRequestBuilders
								.post("/jams/{jamId}/teams/{teamId}/marks", MarkControllerTests.TEST_RATING_JAM_ID,
										MarkControllerTests.TEST_NOTRATING_TEAM_ID)
								.with(SecurityMockMvcRequestPostProcessors.csrf())
								.param("value", "5")
								.param("comments", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
