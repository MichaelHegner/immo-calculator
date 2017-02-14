package ch.hemisoft.immo.domain;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
@Embeddable
public class Address {
	private String street;
	private String streetNumber;

	@NotNull
	@Pattern(regexp = "\\d{4,5}")
	private String zip;
	private String city;
}
