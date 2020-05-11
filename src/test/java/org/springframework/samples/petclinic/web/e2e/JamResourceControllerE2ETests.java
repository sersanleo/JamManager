package org.springframework.samples.petclinic.web.e2e;

import javax.transaction.Transactional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@Transactional
class JamResourceControllerE2ETests {

	private static final int TEST_JAM_ID = 1;
	private static final int TEST_JAMRESOURCE_ID = 1;
	private static final int TEST_NONEXISTENT_JAMRESOURCE_ID = 100;
	private static final int TEST_NONEXISTENT_JAM_ID = 100;
	
	@Autowired
	private MockMvc mockMvc;
	
	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testSuccessfulInitJamResourceCreationForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/new",
						JamResourceControllerE2ETests.TEST_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedInitJamResourceCreationFormNonExistentJam() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/new",
						JamResourceControllerE2ETests.TEST_NONEXISTENT_JAM_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testSuccesfulJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerE2ETests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceCreationEmptyDescription() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerE2ETests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceCreationEmptyUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerE2ETests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceCreationInvalidUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/{jamId}/jamResources/new", JamResourceControllerE2ETests.TEST_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "No es una url"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceCreationNonExistentJam() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/new", JamResourceControllerE2ETests.TEST_NONEXISTENT_JAM_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testSuccessfulInitJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/{jamResourceId}/edit",
						JamResourceControllerE2ETests.TEST_JAM_ID, JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedInitJamResourceUpdateFormNonExistentJamResource() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/{jamId}/jamResources/{jamResourceId}/edit",
						JamResourceControllerE2ETests.TEST_JAM_ID,
						JamResourceControllerE2ETests.TEST_NONEXISTENT_JAMRESOURCE_ID))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testSuccessfulProcessJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "tests cambiado")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedProcessJamResourceUpdateNonExistentResource() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_NONEXISTENT_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "tests cambiado")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceUpdateEmptyDescription() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "description"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}
	
	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceUpdateEmptyUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", ""))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceUpdateInvalidUrl() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.post("/jams/{jamId}/jamResources/{jamResourceId}/edit", JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "No es una url"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.model().attributeHasErrors("jamResource"))
				.andExpect(MockMvcResultMatchers.model().attributeErrorCount("jamResource", 1))
				.andExpect(MockMvcResultMatchers.model().attributeHasFieldErrors("jamResource", "downloadUrl"))
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"));
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testSuccesfulJamResourceRemove() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/jamResources/{jamResourceId}/delete",
								JamResourceControllerE2ETests.TEST_JAM_ID, JamResourceControllerE2ETests.TEST_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}

	@WithMockUser(username="jamOrganizator1", password="jamOrganizator1", authorities = {"jamOrganizator"})
	@Test
	void testFailedJamResourceRemoveNonExistent() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders
						.get("/jams/{jamId}/jamResources/{jamResourceId}/delete",
								JamResourceControllerE2ETests.TEST_JAM_ID,
								JamResourceControllerE2ETests.TEST_NONEXISTENT_JAMRESOURCE_ID)
						.with(SecurityMockMvcRequestPostProcessors.csrf()))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("exception"));
	}
}
