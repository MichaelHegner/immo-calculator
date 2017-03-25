package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PUBLIC;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

import org.springframework.util.Assert;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Data
@ToString(of={"nameOfInstitution", "property"})
@EqualsAndHashCode(of={"nameOfInstitution", "property"})
@NoArgsConstructor(access=PUBLIC)
public class Credit {
	@Id @ GeneratedValue 								Long 		id;
	@Size(min=1, max=255)								String		nameOfInstitution;
	@Min(0)												Double 		interestRateNominalInPercent 			= 0.0;
	@Min(0)												Double 		redemptionAtBeginInPercent 				= 0.0;
	@Min(0)												Double 		specialRedemptionEachYearInPercent 		= 0.0;
	
	@OneToOne(mappedBy="selectedCredit", fetch=LAZY)	Property 	property;
	
	public double getTerm() {
		double financialNeedsTotal = property.getFinancialNeedsTotal();
		double rateEachYear = getAnnuityEachYear() + getSpecialRedemptionEachYearAbsolut();
		return (Math.log(rateEachYear) - Math.log(rateEachYear - financialNeedsTotal * interestRateNominalAsQuote())) / Math.log(1 + interestRateNominalAsQuote());
	}
	
	public double getSpecialRedemptionEachYearAbsolut() {
		return property.getFinancialNeedsTotal() * specialRedemptionEachYearInPercent / 100;
	}
	
	//
	
	public double getRestLoanIn5Years() {
		return getRestLoanInYear(5);
	}	
	
	public double getRestLoanIn10Years() {
		return getRestLoanInYear(10);
	}
	
	public double getRestLoanIn15Years() {
		return getRestLoanInYear(15);
	}
	
	public double getRestLoanIn20Years() {
		return getRestLoanInYear(20);
	}
	
	public double getRestLoanIn25Years() {
		return getRestLoanInYear(25);
	}
	
	public double getRestLoanIn30Years() {
		return getRestLoanInYear(30);
	}
	
	private double getRestLoanInYear(int afterNumberOfYears) {
		Assert.notNull(afterNumberOfYears, "Parameter afterNumberOfYears must not be null.");
		double K = property.getFinancialNeedsTotal();
		double T1 = getRedemptionOfFirstYear() + getSpecialRedemptionEachYearAbsolut();
		double q = 1 + interestRateNominalAsQuote();
		double qPowN = Math.pow(q, afterNumberOfYears + 1);
		double result = K - ( T1 * (qPowN - 1) ) / ( q - 1 );
		return Math.max(result, 0);
	}
	
	// 
	
	private double getRedemptionOfFirstYear() {
		return getAnnuityEachYear() - getInterestOfFirstYear();
	}
	
	private double getInterestOfFirstYear() {
		return property.getFinancialNeedsTotal() * interestRateNominalAsQuote();
	}
	
	/*-
	private double getAnnuityEndOfYear() {
		double K = property.getFinancialNeedsTotal();
		double q = 1 + interestRateNominalAsQuote();
		double qPowN = Math.pow(q, getTerm() + 1);
		return ( K * qPowN * (q - 1) ) / ( q - 1 );
	}
	*/

	//
	
	public double getAnnuityEachMonth() {
		return getAnnuityEachYear() / 12;
	}
	
	private double getAnnuityEachYear() {
		return property.getFinancialNeedsTotal() * sumTilgungAndInterestAsQuote();
	}
	
	//
	
	private double sumTilgungAndInterestAsQuote() {
		return sumTilgungAndInterestInPercent() / 100;
	}

	private double sumTilgungAndInterestInPercent() {
		return interestRateNominalInPercent + redemptionAtBeginInPercent;
	}
	
	private double redemptionAtBeginAsQuote() {
		return redemptionAtBeginInPercent / 100;
	}
	
	private double interestRateNominalAsQuote() {
		return interestRateNominalInPercent / 100;
	}
	
}
