
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Invitation extends BaseEntity {

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Past
	private Date			creationDate;
	
	@NotNull
	private InvitationStatus status;

	// Relationships

	@ManyToOne(optional = false)
	private Team	from;

	@ManyToOne(optional = false)
	private User	to;
}
