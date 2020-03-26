
package org.springframework.samples.petclinic.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "jam_resource")
public class JamResource extends BaseEntity {

	@Column(name = "description")
	@NotBlank
	private String	description;
	
	@NotBlank
	@URL
	@Column(name = "download_url")
	private String	downloadUrl;

	@ManyToOne(optional = false)
	@JoinColumn(name = "jams_id")
	private Jam		jam;
}
