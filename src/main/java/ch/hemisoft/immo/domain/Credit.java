package ch.hemisoft.immo.domain;

import java.math.BigDecimal;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@MappedSuperclass 
@Data
@ToString(of={"nameOfInstitution"})
@EqualsAndHashCode(of={"nameOfInstitution"})
@NoArgsConstructor
public abstract class Credit {
	@Id @ GeneratedValue 								Long 		id;
	@Size(min=1, max=255)								String		nameOfInstitution;
	@Min(0)												BigDecimal 	interestRateNominalInPercent;
	@Min(0)												BigDecimal	redemptionAtBeginInPercent;
	@Min(0)												BigDecimal	specialRedemptionEachYearInPercent;
														
	public BigDecimal getTerm() {
		double financialNeedsTotal = getFinancialNeedsTotal();
		double rateEachYear = getAnnuityEachYear() + getSpecialRedemptionEachYearAbsoluteAsDouble();
		double term = (Math.log(rateEachYear) - Math.log(rateEachYear - financialNeedsTotal * interestRateNominalAsQuote())) / Math.log(1 + interestRateNominalAsQuote());
		return BigDecimalUtils.convert(term);
	}
	
	public BigDecimal getSpecialRedemptionEachYearAbsolut() {
		return BigDecimalUtils.convert(getSpecialRedemptionEachYearAbsoluteAsDouble());
	}

	private double getSpecialRedemptionEachYearAbsoluteAsDouble() {
		double financialNeedsTotal = getFinancialNeedsTotal();
		double specialRedemptionEachYearAsQuote = specialRedemptionEachYearInPercent == null ? 0.0 : specialRedemptionEachYearInPercent.doubleValue() / 100;
		return financialNeedsTotal * specialRedemptionEachYearAsQuote;
	}

	public BigDecimal getSpecialRedemptionTotal() {
		return BigDecimalUtils.convert(getSpecialRedemptionEachYearAbsoluteAsDouble() * getTerm().doubleValue());
	}
	
	//
	
	public BigDecimal getRestLoanIn5Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(5));
	}	
	
	public BigDecimal getRestLoanIn10Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(10));
	}
	
	public BigDecimal getRestLoanIn15Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(15));
	}
	
	public BigDecimal getRestLoanIn20Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(20));
	}
	
	public BigDecimal getRestLoanIn25Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(25));
	}
	
	public BigDecimal getRestLoanIn30Years() {
		return BigDecimalUtils.convert(getRestLoanInYear(30));
	}
	
	private double getRestLoanInYear(int afterNumberOfYears) {
		double K = getFinancialNeedsTotal();
		double T1 = getRedemptionOfFirstYear() + getSpecialRedemptionEachYearAbsoluteAsDouble();
		double q = 1 + interestRateNominalAsQuote();
		double qPowN = getQPowN(afterNumberOfYears - 1);
		double result = K - ( T1 * (qPowN - 1) ) / ( q - 1 );
		return Math.max(result, 0);
	}
	
	// 
	
	public BigDecimal getSumInterestTotal() {
		return BigDecimalUtils.convert(getSumInterestInYear((getTerm().intValue()) + 1));
	}	
	
	public BigDecimal getSumInterestIn5Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(5));
	}	
	
	public BigDecimal getSumInterestIn10Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(10));
	}
	
	public BigDecimal getSumInterestIn15Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(15));
	}
	
	public BigDecimal getSumInterestIn20Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(20));
	}
	
	public BigDecimal getSumInterestIn25Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(25));
	}
	
	public BigDecimal getSumInterestIn30Years() {
		return BigDecimalUtils.convert(getSumInterestInYear(30));
	}
	
	private double getSumInterestInYear(int numberOfYears) {
		double sum = 0;
		
		int year = 0;
		do {
			++year;
			double K = getFinancialNeedsTotal();
			double q = 1 + interestRateNominalAsQuote();
			double qn = getQPowN();
			double qt = getQPowN(year - 1);
			double result = K * ( (qn - qt) * (q - 1) ) / ( qn - 1 );
			sum += Math.max(result, 0);
		} while(year <= numberOfYears);
		
		return sum;
	}

	
	//

	private double getQPowN() {
		return getQPowN(getTerm().doubleValue());
	}

	private double getQPowN(double years) {
		double q = 1 + interestRateNominalAsQuote();
		return Math.pow(q, years + 1);
	}

	//

	private double getRedemptionOfFirstYear() {
		return getAnnuityEachYear() - getInterestOfFirstYear();
	}
	
	private double getInterestOfFirstYear() {
		return getFinancialNeedsTotal() * interestRateNominalAsQuote();
	}

	//
	
	public BigDecimal getAnnuityEachMonth() {
		return BigDecimalUtils.convert(getAnnuityEachYear() / 12);
	}
	
	private double getAnnuityEachYear() {
		return getFinancialNeedsTotal() * sumTilgungAndInterestAsQuote();
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
	
	//
	
	private double getFinancialNeedsTotal() {
		return getProperty() == null ? 0.0 : getProperty().getFinancialNeedsTotal().doubleValue();
	}

	abstract Property getProperty();
}
