
package org.springframework.samples.petclinic.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.JamResource;

public interface JamResourceRepository extends CrudRepository<JamResource, Integer> {
}
