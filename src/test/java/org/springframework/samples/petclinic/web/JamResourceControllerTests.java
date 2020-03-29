package org.springframework.samples.petclinic.web;

import java.time.LocalDateTime;
import java.util.HashSet;
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
import org.springframework.samples.petclinic.datatypes.Phone;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.model.Team;
import org.springframework.samples.petclinic.model.User;
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
		
		BDDMockito.given(this.jamService.findJamById(1))
		.willReturn(jam);
BDDMockito.given(this.jamResourceService.findJamResourceById(1))
		.willReturn(jamResource);
}
	@WithMockUser(value = "spring")
	@Test
	void testInitJamResourceCreationForm() throws Exception {
		this.mockMvc.perform(MockMvcRequestBuilders.get("/jams/1/jamResources/new")).andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}
	@WithMockUser(value = "spring")
	@Test
	void testSuccesfulJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/1/jamResources/new").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "test")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedDescriptionJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/1/jamResources/new").with(SecurityMockMvcRequestPostProcessors.csrf())
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
	void testFailedUrlEmptyJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/1/jamResources/new").with(SecurityMockMvcRequestPostProcessors.csrf())
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
	void testFailedUrlJamResourceCreation() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.post("/jams/1/jamResources/new").with(SecurityMockMvcRequestPostProcessors.csrf())
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
	void testSuccessfulInitJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.get("/jams/1/jamResources/1/edit"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.view().name("jams/createOrUpdateJamResourceForm"))
				.andExpect(MockMvcResultMatchers.model().attributeExists("jamResource"));
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testSuccessfulProcessJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/jamResources/1/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
						.param("description", "tests cambiado")
						.param("downloadUrl", "https://www.google.com/intl/es_ALL/drive/"))
				.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
	
	@WithMockUser(value = "spring")
	@Test
	void testFailedProcessJamResourceUpdateForm() throws Exception {
		this.mockMvc
				.perform(MockMvcRequestBuilders.post("/jams/1/jamResources/1/edit").with(SecurityMockMvcRequestPostProcessors.csrf())
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
	void testSuccesfulJamResourceRemove() throws Exception {
		this.mockMvc.perform(
				MockMvcRequestBuilders.get("/jams/1/jamResources/1/delete").with(SecurityMockMvcRequestPostProcessors.csrf()))
		.andExpect(MockMvcResultMatchers.status().is3xxRedirection());
	}
}