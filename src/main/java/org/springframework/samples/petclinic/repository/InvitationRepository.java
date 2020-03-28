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

package org.springframework.samples.petclinic.repository;

import java.util.Collection;

import org.springframework.dao.DataAccessException;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.samples.petclinic.model.Invitation;

public interface InvitationRepository extends CrudRepository<Invitation, Integer> {

	Invitation findById(int id) throws DataAccessException;

	@Query("SELECT i FROM Invitation i WHERE i.to.username = ?1 AND i.status = org.springframework.samples.petclinic.model.InvitationStatus.PENDING AND i.from.jam.inscriptionDeadline > CURRENT_TIMESTAMP")
	Collection<Invitation> findPendingInvitationsByUsername(String username) throws DataAccessException;

	@Query("SELECT i FROM Invitation i WHERE i.from.jam.id = ?1 AND i.to.username = ?2 AND i.status = org.springframework.samples.petclinic.model.InvitationStatus.PENDING")
	Collection<Invitation> findPendingInvitationsByJamIdAndUsername(int jamId, String username);

	@Modifying
	@Query("DELETE FROM Invitation i WHERE i.from.jam.id = ?1 AND i.to.username = ?2 AND i.status = org.springframework.samples.petclinic.model.InvitationStatus.PENDING")
	void deleteAllPendingInvitationsByJamIdAndUsername(int jamId, String username) throws DataAccessException; // no
																												// user
																												// no
																												// funciona
}
