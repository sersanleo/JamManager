
package org.springframework.samples.petclinic.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery extends BaseEntity {

	private static final long	serialVersionUID	= 1L;

	private String				description;

	@NotBlank
	@URL
	private String				downloadURL;

	@Temporal(TemporalType.TIMESTAMP)
	@NotNull
	@Past
	private Date				creationMoment;

	@ManyToOne(optional = false)
	private Team				team;
}
