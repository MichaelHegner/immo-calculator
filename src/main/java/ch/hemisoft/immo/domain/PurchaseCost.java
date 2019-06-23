package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class PurchaseCost {
    @NotNull @Min(0) @Column(nullable = false) BigDecimal landAcquisition;
    @NotNull @Min(0) @Column(nullable = false) BigDecimal notary;
    @NotNull @Min(0) @Column(nullable = false) BigDecimal agency;
    @NotNull @Min(0) @Column(nullable = false) BigDecimal valuation;
    @NotNull @Min(0) @Column(nullable = false) BigDecimal court;
    
    public double getTotalCompletionCost() {
        double dLandAcquisition = null == landAcquisition   ? 0.0 : landAcquisition.doubleValue();
        double dNotary          = null == notary            ? 0.0 : notary.doubleValue();
        double dAgency          = null == agency            ? 0.0 : agency.doubleValue();
        double dValuation       = null == valuation         ? 0.0 : valuation.doubleValue();
        double dCourt           = null == court             ? 0.0 : court.doubleValue();
        return dLandAcquisition + dNotary + dAgency + dValuation + dCourt;
    }
}
