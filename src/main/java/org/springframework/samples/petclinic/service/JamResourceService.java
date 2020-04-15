
package org.springframework.samples.petclinic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.samples.petclinic.model.JamResource;
import org.springframework.samples.petclinic.repository.JamResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JamResourceService {

	@Autowired
	private JamResourceRepository jamResourceRepository;

	@Transactional(readOnly = true)
	public JamResource findJamResourceById(final int id) throws DataAccessException {
		return this.jamResourceRepository.findById(id).get();
	}

	@Transactional
	public void saveJamResource(final JamResource jamResource) throws DataAccessException {
		this.jamResourceRepository.save(jamResource);
	}

	@Transactional
	public void deleteJamResource(final JamResource jamResource) throws DataAccessException {
		this.jamResourceRepository.delete(jamResource);
	}
}
