
package org.springframework.samples.petclinic.model;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class JamResource extends BaseEntity {

	@NotBlank
	private String	description;

	@NotBlank
	@URL
	private String	downloadUrl;

	@ManyToOne(optional = false)
	private Jam		jam;
}
