package org.springframework.samples.petclinic.service;

import java.util.NoSuchElementException;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.stereotype.Service;

@DataJpaTest(includeFilters = @ComponentScan.Filter(Service.class))
class JamResourceServiceTests {

	@Autowired
	protected JamResourceService jamResourceService;

	@Autowired
	protected JamService jamService;

	@Test
	void shouldFindJamResourceById() {
		JamResource jamResource = this.jamResourceService.findJamResourceById(1);
		Assertions.assertThat(jamResource).isNotEqualTo(null);
	}

	@Test
	void shouldNotFindJamResourceById() {
		Assertions.assertThatThrownBy(() -> this.jamResourceService.findJamResourceById(40))
				.isInstanceOf(NoSuchElementException.class);
	}

	@Test
	public void shouldSaveJamResource() {
		JamResource jamResource = new JamResource();
		Jam jam = this.jamService.findJamById(1);

		jamResource.setDescription("Descripcion de prueba");
		jamResource.setDownloadUrl("https://www.google.com/intl/es_ALL/drive/");
		jamResource.setJam(jam);
		this.jamResourceService.saveJamResource(jamResource);

		Assertions.assertThat(jamResource.getId()).isNotNull();
	}

	@Test
	void shouldUpdateJamResource() {
		JamResource jamResource = this.jamResourceService.findJamResourceById(1);
		String oldDescripiton = jamResource.getDescription();
		String newDescription = oldDescripiton + "X";

		jamResource.setDescription(newDescription);
		this.jamResourceService.saveJamResource(jamResource);

		jamResource = this.jamResourceService.findJamResourceById(1);
		Assertions.assertThat(jamResource.getDescription()).isEqualTo(newDescription);
	}

	@Test
	public void shouldDeleteJamResoruce() {
		JamResource jamResource = this.jamResourceService.findJamResourceById(1);
		this.jamResourceService.deleteJamResource(jamResource);

		Assertions.assertThatThrownBy(() -> this.jamResourceService.findJamResourceById(1))
				.isInstanceOf(NoSuchElementException.class);
	}
}
