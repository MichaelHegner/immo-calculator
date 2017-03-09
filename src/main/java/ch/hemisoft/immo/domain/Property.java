package ch.hemisoft.immo.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.time.LocalDate;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Entity
@Data
public class Property {
	@Id @GeneratedValue
	Long id; 
	
	// ==============================================================================================
	// Basic Data ...
	// ==============================================================================================

							long 			noApartments;
							long 			noParking;
							double 			livingSpaceInQm;
							double 			landAreaInQm;
							Address 		address;

	@NotNull 
	@Min(1800) @Max(2100)	Integer 		yearOfConstruction;
	@NotNull 
	@Enumerated(STRING)		PropertyType 	type;
	
	@DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
	@NotNull LocalDate 		purchaseDate;

	// ==============================================================================================
	// Purchase Costs ...
	// ==============================================================================================

	double 					purchasePrice;
	PurchaseCost 			purchaseCost;
	CompletionCost 			completionCost;
	
	public double getTotalAttendantCost() {
		return purchaseCost.getTotalCompletionCost() + completionCost.getTotalCompletionCost();
	}
	
	public double getTotalPurchaseCost() {
		return purchasePrice + getTotalAttendantCost();
	}

	// ==============================================================================================
	// Management Costs ...
	// ==============================================================================================
	
	RunningCost 		runningCost;
	
	public double getTotalAdministrationCost() {
		return runningCost.getAdministrationEachApartment() * noApartments;
	}
	
	public double getTotalMaintenanceCost() {
		return runningCost.getMaintenanceEachQm() * livingSpaceInQm;
	}
	
	public double getTotalManagementCost() {
		return getTotalAdministrationCost() + getTotalMaintenanceCost();
	}

	// ==============================================================================================
	// Rental ...
	// ==============================================================================================
	
	double rentalNet;
	
	public double getRentalNetAfterManagementCost() {
		return rentalNet - getTotalManagementCost();
	}

	// ==============================================================================================
	// Purchase Factor & Rental ...
	// ==============================================================================================
	
	public double getPurchaseFactor() {
		return getTotalPurchaseCost() / getRentalNetAfterManagementCost();
	}
	
	public double getFirstRental() {
		return getRentalNetAfterManagementCost() / getTotalPurchaseCost() * 100;
	}
	
	// ==============================================================================================
	// Relations ...
	// ==============================================================================================
	
	@ManyToOne(fetch = LAZY, optional = false)		User  	owner;
	
}
