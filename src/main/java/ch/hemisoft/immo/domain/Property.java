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
	
	// ==============================================================================================
	// Basic Data ...
	// ==============================================================================================

	long 					noApartments;
	long 					noParking;
	double 					livingSpaceInQm;
	double 					landAreaInQm;
	Address 				address;

	@NotNull YearMonth 		yearOfConstruction;
	@NotNull PropertyType 	type;
	
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
	
}
