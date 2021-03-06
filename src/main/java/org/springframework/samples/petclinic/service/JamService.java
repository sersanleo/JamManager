/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.samples.petclinic.service;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Jam;
import org.springframework.samples.petclinic.model.JamStatus;
import org.springframework.samples.petclinic.repository.JamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JamService {

	@Autowired
	private JamRepository jamRepository;

	@Transactional(readOnly = true)
	public Jam findJamById(final int id) {
		return this.jamRepository.findById(id).get();
	}

	@Transactional(readOnly = true)
	public Collection<Jam> findJams() {
		return this.jamRepository.findAll();
	}

	@Transactional
	public void saveJam(final Jam jam) {
		this.jamRepository.save(jam);
	}

	@Transactional
	public void deleteJam(final Jam jam) {
		this.jamRepository.delete(jam);
	}

}
