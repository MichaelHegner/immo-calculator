package ch.hemisoft.immo.calc.business.utils;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data
public class AnnuityCalculator {
	@NonNull private final Double creditAmount;
	@NonNull private final Double interestInPercent;
	@NonNull private final Double redemptionInPercentAtBegin;
	@NonNull private Double specialRedemptionInPercent;
	@NonNull private final Integer termInYears;
	
	private final List<Annuity> annuities;

	public AnnuityCalculator(Double creditAmount, Double interestInPercent, Double redemptionInPercentAtBegin, Double specialRedemptionInPercent,
			Integer termInYears) {
		super();
		this.creditAmount = creditAmount;
		this.interestInPercent = interestInPercent;
		this.redemptionInPercentAtBegin = redemptionInPercentAtBegin;
		this.specialRedemptionInPercent = specialRedemptionInPercent;
		this.termInYears = termInYears;
		
		annuities = new ArrayList<>(termInMonths());
		
		for(int i = 0; i <= termInMonths(); i++) {
			final int currentMonth = i + 1;
			if(i == 0) {
				annuities.add(new Annuity(currentMonth, currentMonth / 12.0, interestAsQuote(), 0.0, annuityEachMonth(), creditAmount));
			} else {
				double amountEnd = annuities.get(i-1).getAmountEnd();
				
				if(amountEnd <= 0) break;
				
				if(currentMonth % 12 == 0) {
					annuities.add(new Annuity(currentMonth, currentMonth / 12.0, interestAsQuote(), specialRedemptionAbsolute(), annuityEachMonth(), amountEnd));
				} else {
					annuities.add(new Annuity(currentMonth, currentMonth / 12.0, interestAsQuote(), 0.0, annuityEachMonth(), amountEnd));
				}
			}
		}
		
	}
	
	private int termInMonths() {
		return termInYears * 12;
	}
	
	private double annuityEachMonth() {
		return creditAmount * (interestAsQuote() + redemptionAsQuote()) / 12;
	}
	
	private double quoteInterestSquareTerm() {
		return Math.pow(interestAsQuote() + 1, termInYears);
	}
	
	private double interestAsQuote() {
		return (interestInPercent) / 100;
	}
	
	private double redemptionAsQuote() {
		return (redemptionInPercentAtBegin) / 100;
	}
	
	private double specialRedemptionAbsolute() {
		return creditAmount * (specialRedemptionInPercent) / 100;
	}
	
	//
	
	public static void main(String ... args) {
		AnnuityCalculator calc = new AnnuityCalculator(100000.0, 2.2, 4.0, 1.0, 20);
		calc.getAnnuities().stream().forEach(System.out::println);
	}
	

	@Data
	@RequiredArgsConstructor
	@ToString
	public static class Annuity {
		@NonNull private Integer month;
		@NonNull private Double year;
		@NonNull private Double interest;
		@NonNull private Double specialRedempationAbsolute;
		@NonNull private Double monthlyRate;
		@NonNull private Double amountBegin;
		
		public double getInterest() {
			return amountBegin * interest / 12;
		}
		
		public double getRedemption() {
			return monthlyRate - (amountBegin * interest / 12);
		}
		
		public double getAmountEnd() {
			return Math.max(0, amountBegin - getRedemption() - specialRedempationAbsolute);
		}
	}
}
