package ch.hemisoft.immo.domain;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import ch.hemisoft.immo.validation.PastLocalDate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Data
@ToString(of={"purchaseDate", "address"})
@EqualsAndHashCode(of={"purchaseDate", "address"})
public class Property implements Ownable {
	@Id @GeneratedValue		Long 			id; 
	
	// ==============================================================================================
	// Basic Data ...
	// ==============================================================================================

	@Min(1) @Max(100)		long 			noApartments;
	@Min(0) @Max(100)		long 			noParking;
	@Min(1) @Max(10000)		BigDecimal		livingSpaceInQm;
	@Min(0) @Max(10000)		BigDecimal 		landAreaInQm;
	@NotNull @Valid			Address 		address;

	@NotNull 
	@Min(1800) @Max(2100)	Integer 		yearOfConstruction;
	@NotNull 
	@Enumerated(STRING)		PropertyType 	type;
	
	@DateTimeFormat(iso =  DateTimeFormat.ISO.DATE)
	@PastLocalDate 			LocalDate 		purchaseDate; // TODO: Combined Check Constained with status

	@NotNull 
	@Enumerated(STRING)		PropertyStatus 	status;
	
	// ==============================================================================================
	// Purchase Costs ...
	// ==============================================================================================

	@Min(1) @Max(10000000)	BigDecimal 		purchasePrice;
	@NotNull @Valid			PurchaseCost 	purchaseCost;
	@NotNull @Valid			CompletionCost 	completionCost;
	
	public BigDecimal getTotalAttendantCost() {
		return BigDecimalUtils.convert(getTotalAttendantCostAsDouble());
	}
	
	private double getTotalAttendantCostAsDouble() {
		if (null == purchaseCost || null == completionCost) return 0.0;
		return purchaseCost.getTotalCompletionCost() + completionCost.getTotalCompletionCost();
	}
	
	public BigDecimal getTotalPurchaseCost() {
		return BigDecimalUtils.convert(getTotalPurchaseCostAsDouble());
	}

	private double getTotalPurchaseCostAsDouble() {
		double dPurchasePrice = null == purchasePrice ? 0.0 : purchasePrice.doubleValue();
		return dPurchasePrice + getTotalAttendantCostAsDouble();
	}

	// ==============================================================================================
	// Management Costs ...
	// ==============================================================================================
	
	@NotNull @Valid			RunningCost 	runningCost;
	
	public BigDecimal getTotalAdministrationCost() {
		double administrationEachApartment = null == runningCost || null == runningCost.getAdministrationEachApartment() ? 0.0 : runningCost.getAdministrationEachApartment().doubleValue();
		return BigDecimalUtils.convert(administrationEachApartment * noApartments);
	}
	
	public BigDecimal getTotalMaintenanceCost() {
		double dLivingSpaceInQm = null == livingSpaceInQm ? 0.0 : livingSpaceInQm.doubleValue();
		double dMaintenanceEachQm = null == runningCost || null == runningCost.getMaintenanceEachQm() ? 0.0 : runningCost.getMaintenanceEachQm().doubleValue();
		return BigDecimalUtils.convert(dLivingSpaceInQm * dMaintenanceEachQm);
	}
	
	public BigDecimal getTotalManagementCost() {
		double totalAdministrationCost = getTotalAdministrationCost().doubleValue();
		double totalMaintenanceCost = getTotalMaintenanceCost().doubleValue();
		return BigDecimalUtils.convert(totalAdministrationCost + totalMaintenanceCost);
	}

	// ==============================================================================================
	// Rental ...
	// ==============================================================================================
	
	@Min(0)					BigDecimal		rentalNet;
	
	public BigDecimal getRentalNetAfterManagementCost() {
		return BigDecimalUtils.convert(getRentalNetAfterManagementCostAsDouble());
	}

	private double getRentalNetAfterManagementCostAsDouble() {
		double dRentalNet = null == rentalNet ? 0.0 : rentalNet.doubleValue();
		return dRentalNet - getTotalManagementCost().doubleValue();
	}

	// ==============================================================================================
	// Purchase Factor & Rental ...
	// ==============================================================================================
	
	public BigDecimal getPurchaseFactor() {
		double rentalNetAfterManagementCost = getRentalNetAfterManagementCostAsDouble();
		double totalPurchaseCost = getTotalPurchaseCostAsDouble();
		double purchaseFactor = rentalNetAfterManagementCost > 0.0 ? totalPurchaseCost / rentalNetAfterManagementCost : 0.0;
		return BigDecimalUtils.convert(purchaseFactor);
	}
	
	public BigDecimal getFirstRental() {
		double rentalNetAfterManagementCost = getRentalNetAfterManagementCostAsDouble();
		double totalPurchaseCost = getTotalPurchaseCostAsDouble();
		double firstRental = totalPurchaseCost > 0.0 ? (rentalNetAfterManagementCost / totalPurchaseCost * 100) : 0.0;
		return BigDecimalUtils.convert(firstRental);
	}
	
	// ==============================================================================================
	// Financial Needs ...
	// ==============================================================================================
	@Min(0) 				double		netAssets;
	
	public BigDecimal getFinancialNeedsTotal() {
		return BigDecimalUtils.convert(getFinancialNeedsTotalAsDouble());
	}

	private double getFinancialNeedsTotalAsDouble() {
		return getTotalPurchaseCostAsDouble() - netAssets;
	}
	
	@Valid 
	@OneToOne(fetch = LAZY, cascade = ALL, orphanRemoval = true)
	@JoinTable(
			name 				= "PROPERTY_CREDIT",
			joinColumns 		= @JoinColumn(name="PROPERTY_ID"), 
			inverseJoinColumns 	= @JoinColumn(name = "CREDIT_ID", nullable = false, unique = true)
	)
	ActiveCredit 			selectedCredit;
	
	@Valid 
	@Getter @Setter
	@OneToMany(mappedBy="property", fetch = LAZY, cascade = ALL, orphanRemoval = true)
	Collection<NotActiveCredit> 	creditOptions = new ArrayList<>();	
	

	public void addCreditOptions(NotActiveCredit credit) {
		creditOptions.add(requireNonNull(credit));
	}
	
	// ==============================================================================================
	// Relations ...
	// ==============================================================================================
	
	@ManyToOne(fetch = LAZY, optional = false)		User  	owner;
}
