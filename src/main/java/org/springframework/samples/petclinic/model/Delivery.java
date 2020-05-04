
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.hibernate.validator.constraints.URL;
import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "deliveries")
public class Delivery extends BaseEntity {

	private static final long serialVersionUID = 1L;

	private String description;

	@NotBlank
	@URL
	@Column(name = "download_url")
	private String downloadURL;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-M-d HH:mm")
	@Past
	private LocalDateTime creationDate;

	@ManyToOne(optional = false)
	private Team team;

	public Delivery() {
		super();
		this.creationDate = LocalDateTime.now().minusNanos(1);
	}
}
