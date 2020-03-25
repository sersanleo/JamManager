
package org.springframework.samples.petclinic.model;

import java.time.LocalDateTime;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "teams")
public class Team extends BaseEntity {

	@NotBlank
	@JoinColumn(name = "name")
	private String			name;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
	@Past
	@Column(name = "creation_date") 
	private LocalDateTime	creationDate;

	// Relationships

	@ManyToOne(optional = false)
	@JoinColumn(name = "jam_id")
	private Jam				jam;

	@ManyToMany()
	private Set<User>		members;

	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Mark>		marks;

	@OneToMany(cascade = CascadeType.ALL, mappedBy = "team", fetch = FetchType.EAGER)
	private Set<Delivery>	deliveries;
}
