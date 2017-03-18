package ch.hemisoft.immo.domain;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ch.hemisoft.immo.validation.PastLocalDate;
import lombok.Data;

@Entity
@Data
public class Property implements Ownable{
	@Id @GeneratedValue		Long 			id; 
	
	// ==============================================================================================
	// Basic Data ...
	// ==============================================================================================

	@Min(1) @Max(100)		long 			noApartments;
	@Min(0) @Max(100)		long 			noParking;
	@Min(1) @Max(10000)		double 			livingSpaceInQm;
	@Min(0) @Max(10000)		double 			landAreaInQm;
	@NotNull @Valid			Address 		address;

	@NotNull 
	@Min(1800) @Max(2100)	Integer 		yearOfConstruction;
	@NotNull 
	@Enumerated(STRING)		PropertyType 	type;
	
	@DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
	@NotNull @PastLocalDate LocalDate 		purchaseDate;

	// ==============================================================================================
	// Purchase Costs ...
	// ==============================================================================================

	@Min(1) @Max(10000000)	double 			purchasePrice;
	@NotNull @Valid			PurchaseCost 	purchaseCost;
	@NotNull @Valid			CompletionCost 	completionCost;
	
	public double getTotalAttendantCost() {
		return purchaseCost.getTotalCompletionCost() + completionCost.getTotalCompletionCost();
	}
	
	public double getTotalPurchaseCost() {
		return purchasePrice + getTotalAttendantCost();
	}

	// ==============================================================================================
	// Management Costs ...
	// ==============================================================================================
	
	@NotNull @Valid			RunningCost 	runningCost;
	
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
	
	@Min(0)					double 			rentalNet;
	
	public double getRentalNetAfterManagementCost() {
		return rentalNet - getTotalManagementCost();
	}

	// ==============================================================================================
	// Purchase Factor & Rental ...
	// ==============================================================================================
	
	public double getPurchaseFactor() {
		double rentalNetAfterManagementCost = getRentalNetAfterManagementCost();
		double totalPurchaseCost = getTotalPurchaseCost();
		return rentalNetAfterManagementCost > 0.0 ? totalPurchaseCost / rentalNetAfterManagementCost : 0.0;
	}
	
	public double getFirstRental() {
		double rentalNetAfterManagementCost = getRentalNetAfterManagementCost();
		double totalPurchaseCost = getTotalPurchaseCost();
		return totalPurchaseCost > 0.0 ? (rentalNetAfterManagementCost / totalPurchaseCost * 100) : 0.0;
	}
	
	// ==============================================================================================
	// Financial Needs ...
	// ==============================================================================================

	@Min(0) 				double			netAssets;
	
	public double getFinancialNeedsTotal() {
		return getTotalPurchaseCost() - netAssets;
	}
	
							FinancingCredit 			selectedCredit;
	@ElementCollection		Collection<FinancingCredit> creditOptions = new ArrayList<>();						
	
	
	// ==============================================================================================
	// Relations ...
	// ==============================================================================================
	
	@ManyToOne(fetch = LAZY, optional = false)		User  	owner;
}
