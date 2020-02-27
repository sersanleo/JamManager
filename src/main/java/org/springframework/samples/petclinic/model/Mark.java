
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Range;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Mark extends BaseEntity {

	@Range(min = 0, max = 10)
	@NotNull
	private Float	mark;

	// Relationships

	@ManyToOne(optional = false)
	private User	giver;

	@ManyToOne(optional = false)
	private Team	team;
}
