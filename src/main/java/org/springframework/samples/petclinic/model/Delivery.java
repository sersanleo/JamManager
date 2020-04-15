
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Delivery extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String description;

	@NotBlank
	@URL
	private String downloadURL;

	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	private LocalDateTime creationDate;

	@ManyToOne(optional = false)
	private Team team;

	public Delivery() {
		super();
		this.creationDate = LocalDateTime.now().minusNanos(1);
	}
}
