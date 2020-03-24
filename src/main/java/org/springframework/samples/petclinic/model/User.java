
package org.springframework.samples.petclinic.model;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.samples.petclinic.datatypes.Phone;

import lombok.Data;

@Data
@Entity
@Table(name = "users")
public class User {

	@Id
	String username;

	@NotBlank
	String password;

	boolean enabled;

	@Email
	@NotBlank
	String email;

	@NotNull
	@Valid
	@AttributeOverrides({ @AttributeOverride(name = "countryCode", column = @Column(name = "phone_country_code")),
			@AttributeOverride(name = "areaCode", column = @Column(name = "phone_area_code")),
			@AttributeOverride(name = "number", column = @Column(name = "phone_number")) })
	Phone phone;
	
	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;
	
	
}
