package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.NoSuchElementException;

import org.assertj.core.util.Arrays;
import org.assertj.core.util.Lists;
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
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Mark;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.samples.petclinic.service.TeamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = JamController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class JamControllerTests {

	private static final int TEST_INSCRIPTION_JAM_ID = 1;
	private static final int TEST_CANCELLED_JAM_ID = 6;
	private static final int TEST_RATING_JAM_ID = 4;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;
	private static final int TEST_TEAM1_ID = 1;
	private static final int TEST_TEAM2_ID = 2;
	private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-M-d HH:mm");

	@MockBean
	private JamService jamService;

	@MockBean
	private TeamService teamService;

	@Autowired
	private MockMvc mockMvc;

	private static final LocalDateTime futureDateTime(final int daysOffset) {
		return LocalDateTime.now().plusDays(daysOffset);
	}

	private static final String futureDateTimeToString(final int daysOffset) {
		return JamControllerTests.DATETIME_FORMATTER.format(JamControllerTests.futureDateTime(daysOffset));
	}

	private static final Jam buildJam(String name, int inscriptionDeadlineOffset, int startOffset, int endOffset) {
		Jam res = new Jam();
		res.setName(name);
		res.setDescription("This is a test Jam.");
		res.setDifficulty(5);
		res.setInscriptionDeadline(JamControllerTests.futureDateTime(inscriptionDeadlineOffset));
		res.setMaxTeamSize(5);
		res.setMinTeams(2);
		res.setMaxTeams(12);
		res.setStart(JamControllerTests.futureDateTime(startOffset));
		res.setEnd(JamControllerTests.futureDateTime(endOffset));
		res.setTeams(new HashSet<Team>());
		res.setJamResources(new HashSet<JamResource>());
		return res;
	}

	private static final Mark buildMark(float value) {
		Mark res = new Mark();
		res.setValue(value);
		return res;
	}

	private static final Team buildTeam(int id, String name, Mark... marks) {
		Team res = new Team();
		res.setId(id);
		res.setName(name);
		res.setMarks(new HashSet(Arrays.asList(marks)));
		return res;
	}

	@BeforeEach
	private void beforeEach() {
		Jam inscriptionJam = buildJam("Inscription Jam", 5, 7, 9);
		Jam cancelledJam = buildJam("Cancelled Jam", -1, 7, 9);

		Jam ratingJam = buildJam("Rating Jam", -7, -5, -3);
		ratingJam.setTeams(new HashSet<Team>() {
			{
				add(buildTeam(TEST_TEAM1_ID, "Team 1", buildMark(4)));
				add(buildTeam(TEST_TEAM2_ID, "Team 2", buildMark(4)));
			}
		});

		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.willReturn(inscriptionJam);
		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_CANCELLED_JAM_ID))
				.willReturn(cancelledJam);
		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_RATING_JAM_ID))
				.willReturn(ratingJam);
		BDDMockito.given(this.jamService.findJams())
				.willReturn(Lists.newArrayList(inscriptionJam, cancelledJam, ratingJam));

		BDDMockito.given(this.jamService.findJamById(JamControllerTests.TEST_NONEXISTENT_JAM_ID))
				.willThrow(NoSuchElementException.class);
	}

	@WithMockUser(value = "spring")
	@Test
	void testListJams() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/jamList"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jams"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(ints = { JamControllerTests.TEST_INSCRIPTION_JAM_ID, JamControllerTests.TEST_CANCELLED_JAM_ID })
	@ParameterizedTest
	void testSuccessfulShowJam(final int jamId) throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}", jamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/jamDetails"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedShowJamNonExistent() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}", JamControllerTests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testInitJamCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulJamCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamCreationNameError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "name"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamCreationDescriptionError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "-1", "0", "6", "aegg" })
	@ParameterizedTest
	void testFailedJamCreationDifficultyError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", value)
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "difficulty"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(ints = { -5, -1, 8, 10 })
	@ParameterizedTest
	void testFailedJamCreationInscriptionDeadlineError(final int offset) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(offset))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "inscriptionDeadline"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "-1", "0" })
	@ParameterizedTest
	void testFailedJamCreationMaxTeamSizeError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", value)
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "maxTeamSize"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "-1", "0", "1", "3" })
	@ParameterizedTest
	void testFailedJamCreationMinTeamsError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", value)
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "minTeams"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(strings = { "-1", "0", "1" })
	@ParameterizedTest
	void testFailedJamCreationMaxTeamsError(final String value) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", value)
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jam", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "maxTeams"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamCreationStartError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(-1))
						.param("end", JamControllerTests.futureDateTimeToString(9)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "start"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamCreationEndError() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("name", "test")
						.param("description", "test")
						.param("difficulty", "1")
						.param("inscriptionDeadline", JamControllerTests.futureDateTimeToString(5))
						.param("maxTeamSize", "1")
						.param("minTeams", "2")
						.param("maxTeams", "2")
						.param("start", JamControllerTests.futureDateTimeToString(7))
						.param("end", JamControllerTests.futureDateTimeToString(-1)))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "end"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitJamUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", JamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitJamUpdateFormNotInscriptionJam() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", JamControllerTests.TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitJamUpdateFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", JamControllerTests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulDeleteJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/delete", JamControllerTests.TEST_INSCRIPTION_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedDeleteJamNotInscriptionJam() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", JamControllerTests.TEST_CANCELLED_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedDeleteJamNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/edit", JamControllerTests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulShowPublishResults() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/jams/{jamId}/publish", JamControllerTests.TEST_RATING_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jam"));
	}

	@WithMockUser(value = "spring")
	@ValueSource(ints = { TEST_INSCRIPTION_JAM_ID, TEST_CANCELLED_JAM_ID })
	@ParameterizedTest
	void testFailedShowPublishResultsNotRating(int jamId) throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/jams/{jamId}/publish", jamId))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulPublishResults() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection())
				.andExpect(MockMvcResultMatchers.view().name("redirect:/jams/{jamId}"));
	}

	@WithMockUser(value = "spring")
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

	@WithMockUser(value = "spring")
	@Test
	void testFailedPublishResultsAtLeastOneMark() throws Exception {
		Jam ratingJam = this.jamService.findJamById(TEST_RATING_JAM_ID);
		ratingJam.getTeams().iterator().next().getMarks().clear();
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "winner.id"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("jam", "winner.id", "atLeast1"))
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedPublishResultsNeedsSameNumberOfMarks() throws Exception {
		Jam ratingJam = this.jamService.findJamById(TEST_RATING_JAM_ID);
		ratingJam.getTeams().iterator().next().getMarks().add(buildMark(8));
		
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/publish", TEST_RATING_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("winner.id", "1"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jam"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jam", "winner.id"))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrorCode("jam", "winner.id", "sameNumberOfMarks"))
				.andExpect(MockMvcResultMatchers.view().name("jams/publishResultsForm"));
	}
}
