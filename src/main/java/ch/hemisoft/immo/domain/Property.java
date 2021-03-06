package ch.hemisoft.immo.domain;

import static java.util.Objects.requireNonNull;
import static javax.persistence.CascadeType.ALL;
import static javax.persistence.EnumType.STRING;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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

import static org.springframework.format.annotation.DateTimeFormat.ISO.*;

@Entity
@org.hibernate.envers.Audited
@ToString(of={"purchaseDate", "address"})
@EqualsAndHashCode(of={"purchaseDate", "address"})
@Data
public class Property implements Ownable {
    @Id @GeneratedValue(strategy = IDENTITY) Long id; 
    
    // ==============================================================================================
    // Basic Data ...
    // ==============================================================================================

    @NotNull @Min(1) @Max(100)   @Column(nullable = false)    long            noApartments;
    @NotNull @Min(0) @Max(100)   @Column(nullable = false)    long            noParking;
    @NotNull @Min(1) @Max(10000) @Column(nullable = false)    BigDecimal      livingSpaceInQm;
    @NotNull @Min(0) @Max(10000) @Column(nullable = false)    BigDecimal      landAreaInQm;
    @NotNull @Valid                                           Address         address;
    @NotNull @Min(1800) @Max(2100) @Column(nullable = false)  Integer         yearOfConstruction;

    @NotNull @Enumerated(STRING) @Column(nullable = false)    PropertyType    type;
    @NotNull @Enumerated(STRING) @Column(nullable = false)    PropertyStatus  status;
    
    @DateTimeFormat(iso=DATE)
    @PastLocalDate               @Column(nullable = false)    LocalDate       purchaseDate; // TODO: Combined Check Constained with status

    
    // ==============================================================================================
    // Purchase Costs ...
    // ==============================================================================================

    @NotNull @Min(1) @Max(10000000) @Column(nullable = false) BigDecimal      purchasePrice;
    @NotNull @Valid                 @Column(nullable = false) PurchaseCost    purchaseCost;
    @NotNull @Valid                 @Column(nullable = false) CompletionCost  completionCost;
    
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
    
    @NotNull @Valid                 @Column(nullable = false) RunningCost     runningCost;
    
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
    
    @NotNull @Min(0)                @Column(nullable = false) BigDecimal      rentalNet;
    
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
    @Min(0)                         @Column(nullable = false) double        netAssets;
    
    public BigDecimal getFinancialNeedsTotal() {
        return BigDecimalUtils.convert(getFinancialNeedsTotalAsDouble());
    }

    private double getFinancialNeedsTotalAsDouble() {
        return getTotalPurchaseCostAsDouble() - netAssets;
    }
    
    @Valid 
    @Getter @Setter
    @org.hibernate.envers.NotAudited
    @OneToMany(mappedBy="property", fetch = LAZY, cascade = ALL, orphanRemoval = true)
    Collection<Credit>     credits = new ArrayList<>();    
    
    public void addCreditOptions(Credit credit) {
        credits.add(requireNonNull(credit));
    }
    
    public Collection<Credit> getActiveCredits() {
        return getFilteredCredits(Credit::isActive);
    }
    
    public Collection<Credit> getDeactivatedCredits() {
        return getFilteredCredits(Credit::isDeactivated);
    }
    
    private Collection<Credit> getFilteredCredits(Predicate<Credit> predicate) {
        return credits.stream().filter(predicate).collect(Collectors.toList());
    }
    
    
    // ==============================================================================================
    // Relations ...
    // ==============================================================================================
    
    @org.hibernate.envers.NotAudited
    @ManyToOne(fetch = LAZY, optional = false)        User      owner;
}
