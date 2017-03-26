package ch.hemisoft.immo.domain;

import static javax.persistence.FetchType.LAZY;
import static lombok.AccessLevel.PUBLIC;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

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

	public double getSpecialRedemptionTotal() {
		return getSpecialRedemptionEachYearAbsolut() * getTerm();
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
		double K = property.getFinancialNeedsTotal();
		double T1 = getRedemptionOfFirstYear() + getSpecialRedemptionEachYearAbsolut();
		double q = 1 + interestRateNominalAsQuote();
		double qPowN = getQPowN(afterNumberOfYears - 1);
		double result = K - ( T1 * (qPowN - 1) ) / ( q - 1 );
		return Math.max(result, 0);
	}
	
	// 
	
	public double getSumInterestIn5Years() {
		return getSumInterestInYear(5);
	}	
	
	public double getSumInterestIn10Years() {
		return getSumInterestInYear(10);
	}
	
	public double getSumInterestIn15Years() {
		return getSumInterestInYear(15);
	}
	
	public double getSumInterestIn20Years() {
		return getSumInterestInYear(20);
	}
	
	public double getSumInterestIn25Years() {
		return getSumInterestInYear(25);
	}
	
	public double getSumInterestIn30Years() {
		return getSumInterestInYear(30);
	}
	
	private double getSumInterestInYear(int numberOfYears) {
		double sum = 0;
		
		int year = 0;
		do {
			++year;
			double K = property.getFinancialNeedsTotal();
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
		return getQPowN(getTerm());
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
		return property.getFinancialNeedsTotal() * interestRateNominalAsQuote();
	}

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
	
	private double interestRateNominalAsQuote() {
		return interestRateNominalInPercent / 100;
	}
	
}
