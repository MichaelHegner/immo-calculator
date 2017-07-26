package ch.hemisoft.commons.calc.utils;

public final class AnnuitiesCalculator {
	
	/**
	 * Calculates the redemption at the end of the given year
	 * @param K as capital
	 * @param zInterest to pay for the loan (e.g. 4%).
	 * @param zRedemptionAtBegin redemption reducing the loan (e.g. 4%).
	 * @param zSpecialRedemption also reducing loan by free will (e.g. 4%).
	 * @param endOfYear the end of the year to calculate the rest of the capital
	 * @return the redemption at the end of the given year.
	 */
	public static double calculateRedemptionAfterYear(double K, double zInterest, double zRedemptionAtBegin, double zSpecialRedemption, int endOfYear) {
		double annuity = calculateAnnuityInYear(K, zInterest , zRedemptionAtBegin + zSpecialRedemption);
		double interest = calculateInterestInYear(K, zInterest, zRedemptionAtBegin, zSpecialRedemption, endOfYear);
		return annuity - interest;
	}
	
	/**
	 * Calculates the interest in the given year p.a.
	 * @param K as capital
	 * @param t the year to calculate
	 * @param n complete time loan running in years
	 * @param z as percent like 2.2%
	 * @return the interest to pay in the given year.
	 */
	public static double calculateInterestAfterYear(double K, int t, double n, double z) {
		double q = 1 + (z / 100);
		double qn = BasicCalculator.accumulationFactorByYearsAndInterest(n + 1, z);
		double qt = BasicCalculator.accumulationFactorByYearsAndInterest(t, z);
		double result = K * ( (qn - qt) * (q - 1) ) / ( qn - 1 );
		return Math.max(result, 0);
	}
	
	/**
	 * Calculates the intestest at the end of the given year
	 * @param K as capital
	 * @param zInterest to pay for the loan (e.g. 4%).
	 * @param zRedemption redemption reducing the loan (e.g. 4%).
	 * @param zSpecialRedemption also reducing loan by free will (e.g. 4%).
	 * @param endOfYear the end of the year to calculate the rest of the loan
	 * @return the interest at the end of the given year.
	 */
	public static double calculateInterestInYear(double K, double zInterest, double zRedemption, double zSpecialRedemption, int endOfYear) {
		double restLoan = calculateRestLoanInYear(K, zInterest, zRedemption, zSpecialRedemption, endOfYear);
		return BasicCalculator.calculateInterestOfAmount(restLoan, zInterest); 
	}
	
	/**
	 * Calculates the rest loan at the end of the given year.
	 * @param K as capital
	 * @param zInterest to pay for the loan (e.g. 4%).
	 * @param zRedemption redemption reducing the loan (e.g. 4%).
	 * @param zSpecialRedemption also reducing loan by free will (e.g. 4%).
	 * @param endOfYear the end of the year to calculate the rest of the loan
	 * @return the rest loan at the end of the given year.
	 */
	public static double calculateRestLoanInYear(double K, double zInterest, double zRedemption, double zSpecialRedemption, int endOfYear) {
		double redemptionOfFirstYear = calculateRedemptionAmountOfFirstYear(K, zInterest, zRedemption);
		double T1 = redemptionOfFirstYear + calculateSpecialRedemptionInYear(K, zSpecialRedemption);
		double qPowN = BasicCalculator.accumulationFactorByYearsAndInterest(endOfYear, zInterest);
		double result = K - ( T1 * (qPowN - 1) ) / ( zInterest );
		return Math.max(result, 0);
	}
	
	//
	
	/**
	 * Calculates the special redemption (german: Sondertilgung) as amount in the year.
	 * @param K as capital
	 * @param zSpecialRedemption as special redemption in perscent number (e.g. 4%)
	 * @return the special redemption as amount in the year.
	 */
	public static double calculateSpecialRedemptionInYear(double K, double zSpecialRedemption) {
		return BasicCalculator.calculateInterestOfAmount(K, zSpecialRedemption);
	}
	
	/**
	 * Calculates the annuity (german: Annuität) as amount in the year.
	 * @param K as capital
	 * @param zInterest in percent number (e.g. 4% interest)
	 * @param zRedemption in percent number (e.g. 4% redemption)
	 * @return the annuity of the year as amount.
	 */
	public static double calculateAnnuityInYear(double K, double zInterest, double zRedemption) {
		return BasicCalculator.calculateInterestOfAmount(K, zInterest + zRedemption);
	}
	
	//
	
	/**
	 * Calculates the redemption (german: Tilgung) of the first year as amount.
	 * @param K as capital
	 * @param zInterest in percent number (e.g. 4% interest)
	 * @param zRedemption in percent number (e.g. 4% redemption)
	 * @return the redemption of the first year as amount.
	 */
	public static double calculateRedemptionAmountOfFirstYear(double K, double zInterest, double zRedemption) {
		return calculateAnnuityInYear(K, zInterest, zRedemption) - calculateInterestAmountOfFirstYear(K, zInterest);
	}
	
	/**
	 * Calculates the Interest in the first year as absolute amount
	 * @param K as capital 
	 * @param z as interest (in % e.g. 2%)
	 * @return the amount of the interest in the first year
	 */
	public static double calculateInterestAmountOfFirstYear(double K, double z) {
		return BasicCalculator.calculateInterestOfAmount(K, z);
	}
	
	private AnnuitiesCalculator() {}
}
