package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

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
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.service.JamResourceService;
import org.springframework.samples.petclinic.service.JamService;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(controllers = JamResourceController.class, excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = WebSecurityConfigurer.class), excludeAutoConfiguration = SecurityConfiguration.class)
class JamResourceControllerTests {
	private static final int TEST_JAM_ID = 1;
	private static final int TEST_JAMRESOURCE_ID = 1;
	private static final int TEST_NONEXISTENT_JAMRESOURCE_ID = 100;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;

	@MockBean
	private JamService jamService;

	@MockBean
	private JamResourceService jamResourceService;

	@Autowired
	private MockMvc mockMvc;

	@BeforeEach
	private void beforeEach() {
		Jam jam = new Jam();
		JamResource jamResource = new JamResource();
		Set<JamResource> jamResources = new HashSet<>();
		jamResources.add(jamResource);

		jam.setName("Inscription Jam");
		jam.setDescription("This is a test Jam.");
		jam.setDifficulty(5);
		jam.setInscriptionDeadline(LocalDateTime.now().plusDays(3));
		jam.setMaxTeamSize(5);
		jam.setMinTeams(5);
		jam.setMaxTeams(12);
		jam.setStart(LocalDateTime.now().plusDays(5));
		jam.setEnd(LocalDateTime.now().plusDays(8));
		jam.setTeams(new HashSet<Team>());
		jam.setJamResources(jamResources);

		jamResource.setDescription("Description test");
		jamResource.setDownloadUrl("https://www.google.com/intl/es_ALL/drive/");
		jamResource.setJam(jam);

		BDDMockito.given(this.jamService.findJamById(JamResourceControllerTests.TEST_JAM_ID))
				.willReturn(jam);
		BDDMockito.given(this.jamResourceService.findJamResourceById(JamResourceControllerTests.TEST_JAMRESOURCE_ID))
				.willReturn(jamResource);
		BDDMockito.given(this.jamService.findJamById(JamResourceControllerTests.TEST_NONEXISTENT_JAM_ID))
				.willThrow(NoSuchElementException.class);
		BDDMockito
				.given(this.jamResourceService
						.findJamResourceById(JamResourceControllerTests.TEST_NONEXISTENT_JAMRESOURCE_ID))
				.willThrow(NoSuchElementException.class);
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitJamResourceCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/new",
						JamResourceControllerTests.TEST_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitJamResourceCreationFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/new",
						JamResourceControllerTests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerTests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceCreationEmptyDescription() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerTests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceCreationEmptyUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerTests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceCreationInvalidUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerTests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "No es una url"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceCreationNonExistentJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/new", JamResourceControllerTests.TEST_NONEXISTENT_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulInitJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/{jamResourceId}/edit",
						JamResourceControllerTests.TEST_JAM_ID, JamResourceControllerTests.TEST_JAMRESOURCE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedInitJamResourceUpdateFormNonExistentJamResource() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/{jamResourceId}/edit",
						JamResourceControllerTests.TEST_JAM_ID,
						JamResourceControllerTests.TEST_NONEXISTENT_JAMRESOURCE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulProcessJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "tests cambiado")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedProcessJamResourceUpdateNonExistentResource() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_NONEXISTENT_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "tests cambiado")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceUpdateEmptyDescription() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceUpdateEmptyUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceUpdateInvalidUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "No es una url"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulJamResourceRemove() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/jamResources/{jamResourceId}/delete",
								JamResourceControllerTests.TEST_JAM_ID, JamResourceControllerTests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(value = "spring")
	@Test
	void testFailedJamResourceRemoveNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/jamResources/{jamResourceId}/delete",
								JamResourceControllerTests.TEST_JAM_ID,
								JamResourceControllerTests.TEST_NONEXISTENT_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}