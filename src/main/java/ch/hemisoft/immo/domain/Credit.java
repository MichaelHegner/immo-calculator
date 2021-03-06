package ch.hemisoft.immo.domain;

import static java.lang.Boolean.TRUE;
import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import net.hemisoft.financial.calculator.library.utils.AnnuityCalculator;

@Entity
@org.hibernate.envers.Audited
@ToString(of={"nameOfInstitution", "property"})
@EqualsAndHashCode(of={"nameOfInstitution", "property"})
@NoArgsConstructor
@RequiredArgsConstructor
@Data
public class Credit {
    @Id @GeneratedValue(strategy = IDENTITY)            Long        id;
    @Size(min = 1, max = 255) @Column(nullable = false) String      nameOfInstitution;
    @Min(0) @Column(nullable = false)                   BigDecimal  capital                             = BigDecimal.ZERO;
    @Min(0) @Column(nullable = false)                   BigDecimal  interestRateNominalInPercent        = BigDecimal.ZERO;
    @Min(0) @Column(nullable = false)                   BigDecimal  redemptionAtBeginInPercent          = BigDecimal.ZERO;            
    @Min(0) @Column(nullable = false)                   BigDecimal  specialRedemptionEachYearInPercent  = BigDecimal.ZERO;
            @Column(nullable = false)                   Boolean     active                              = Boolean.FALSE;
    
    @ManyToOne(fetch = LAZY, optional = false)
    @JoinTable(
            name                = "PROPERTY_CREDIT",
            joinColumns         = @JoinColumn(name="CREDIT_ID",   nullable = false, foreignKey = @ForeignKey(name = "FK_PROPERTY_CREDIT_OPTIONS_TO_CREDIT"), unique = true),
            inverseJoinColumns  = @JoinColumn(name="PROPERTY_ID", nullable = false, foreignKey = @ForeignKey(name = "FK_PROPERTY_CREDIT_OPTIONS_TO_PROPERTY")), 
            foreignKey          = @ForeignKey(name="FK_PROPERTY_CREDIT_OPTIONS_TO_CREDIT"),
            inverseForeignKey   = @ForeignKey(name="FK_PROPERTY_CREDIT_OPTIONS_TO_PROPERTY")
    )
    
    @org.hibernate.envers.NotAudited
    @NotNull @NonNull                                   Property     property;
    
    public void activate()    { active = TRUE; }
    public void deactivate()  { active = Boolean.FALSE; }
    
    public boolean isDeactivated() { return !isActive(); }
    public boolean isActive()      { return active.booleanValue(); }
                                                        
    public BigDecimal getTerm() {
        double financialNeedsTotal = getCapitalAsDouble();
        double rateEachYear = getAnnuityEachYear().doubleValue() + getSpecialRedemptionEachYearAbsoluteAsDouble();
        double term = (Math.log(rateEachYear) - Math.log(rateEachYear - financialNeedsTotal * interestRateNominalAsQuote())) / Math.log(1 + interestRateNominalAsQuote());
        return BigDecimalUtils.convert(term);
    }
    
    public BigDecimal getSpecialRedemptionEachYearAbsolut() {
        return BigDecimalUtils.convert(getSpecialRedemptionEachYearAbsoluteAsDouble());
    }

    private double getSpecialRedemptionEachYearAbsoluteAsDouble() {
        double financialNeedsTotal = getCapitalAsDouble();
        double specialRedemptionEachYearAsQuote = specialRedemptionEachYearInPercent == null ? 0.0 : specialRedemptionEachYearInPercent.doubleValue() / 100;
        return financialNeedsTotal * specialRedemptionEachYearAsQuote;
    }

    public BigDecimal getSpecialRedemptionTotal() {
        return BigDecimalUtils.convert(getSpecialRedemptionEachYearAbsoluteAsDouble() * getTerm().doubleValue());
    }
    
    //
    
    public BigDecimal getRestLoanIn5Years()  { return BigDecimalUtils.convert(getRestLoanInYear(5));  }    
    public BigDecimal getRestLoanIn10Years() { return BigDecimalUtils.convert(getRestLoanInYear(10)); }
    public BigDecimal getRestLoanIn15Years() { return BigDecimalUtils.convert(getRestLoanInYear(15)); }
    public BigDecimal getRestLoanIn20Years() { return BigDecimalUtils.convert(getRestLoanInYear(20)); }
    public BigDecimal getRestLoanIn25Years() { return BigDecimalUtils.convert(getRestLoanInYear(25)); }
    public BigDecimal getRestLoanIn30Years() { return BigDecimalUtils.convert(getRestLoanInYear(30)); }
    
    private double getRestLoanInYear(int afterNumberOfYears) {
        double K = getCapitalAsDouble();                      
        double interest = getDInterestRateNominalInPercent(); 
        return AnnuityCalculator.calculateRestLoanInYear(K, interest, redemptionAtBeginInPercent.doubleValue(), specialRedemptionEachYearInPercent.doubleValue(), afterNumberOfYears);
    }
    
    // 
    
    public BigDecimal getSumInterestTotal() {
        return BigDecimalUtils.convert(getSumInterestInYear((getTerm().intValue()) + 1));
    }    
    
    public BigDecimal getSumInterestIn5Years()  { return BigDecimalUtils.convert(getSumInterestInYear(5));  }    
    public BigDecimal getSumInterestIn10Years() { return BigDecimalUtils.convert(getSumInterestInYear(10)); }
    public BigDecimal getSumInterestIn15Years() { return BigDecimalUtils.convert(getSumInterestInYear(15)); }
    public BigDecimal getSumInterestIn20Years() { return BigDecimalUtils.convert(getSumInterestInYear(20)); }
    public BigDecimal getSumInterestIn25Years() { return BigDecimalUtils.convert(getSumInterestInYear(25)); }
    public BigDecimal getSumInterestIn30Years() { return BigDecimalUtils.convert(getSumInterestInYear(30)); }
    
    private double getSumInterestInYear(int numberOfYears) {
        double sum = 0;
        
        int year = 0;
        do {
            sum += AnnuityCalculator
                    .calculateInterestAfterYear(getCapitalAsDouble(), getDInterestRateNominalInPercent(), ++year, getTerm().intValue());
        } while(year <= numberOfYears);
        
        return sum;
    }

    //
    
    public BigDecimal getAnnuityEachMonth() {
        return BigDecimalUtils.convert(getAnnuityEachYear().doubleValue() / 12);
    }
    
    private BigDecimal getAnnuityEachYear() {
        return BigDecimalUtils.convert(getCapitalAsDouble() * sumTilgungAndInterestAsQuote());
    }
    
    //
    
    private double sumTilgungAndInterestAsQuote() {
        return sumTilgungAndInterestInPercent() / 100;
    }

    private double sumTilgungAndInterestInPercent() {
        double dRedemptionAtBeginInPercent = null == redemptionAtBeginInPercent ? 0.0 : redemptionAtBeginInPercent.doubleValue();
        return getDInterestRateNominalInPercent() + dRedemptionAtBeginInPercent;
    }
    
    private double interestRateNominalAsQuote() {
        return getDInterestRateNominalInPercent() / 100;
    }

    private double getDInterestRateNominalInPercent() {
        return null == interestRateNominalInPercent ? 0.0 : interestRateNominalInPercent.doubleValue();
    }
    
    private double getCapitalAsDouble() {
        return capital == null ? 0.0 : capital.doubleValue();
    }

}
