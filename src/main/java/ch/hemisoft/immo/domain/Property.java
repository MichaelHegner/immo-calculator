package ch.hemisoft.immo.domain;

import java.time.LocalDate;
import java.time.YearMonth;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Property {
	@Id @GeneratedValue
	Long id; 
	long noApartments;
	long noParking;
	double livingSpaceInQm;
	double landAreaInQm;
	Address address;
	
	@NotNull YearMonth yearOfConstruction;
	@NotNull PropertyType type;
	
	@DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
	@NotNull LocalDate purchaseDate;

	
	// Value ...
	double purchasePrice;
//	PurchaseCost cost;
}
