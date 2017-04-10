package ch.hemisoft.immo.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString
@EqualsAndHashCode
@NoArgsConstructor
public class ForecastConfiguration {
	@Id @ GeneratedValue 			Long 	id;
	@NotNull @Column(unique=true)	String 	countryCode; // TODO: CHECK VALID CODE
	@NotNull						Double 	taxQuote;
	@NotNull						Double 	runningCostIndex;
	@NotNull						Double 	deprecation;
	@NotNull						Double 	rentalIncreaseAllTwoYears;
}