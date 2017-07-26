package ch.hemisoft.commons.calc.utils;

public final class BasicCalculator {
	
	/**
	 * Calculates the Interest of an amount as absolute value.
	 * @param K as capital
	 * @param z as interest
	 * @param t as year
	 * @param interval the interval of years for the next interest increase.
	 * @return returns the interest as absolute value.
	 */
	public static double calculateInterestOfAmountWithInterval(double K, double z, int t, int interval) {
		double tInterval = t %  interval == 0 ? t :  t - t %  interval;
		double accumulationFactor = accumulationFactorByYearsAndInterest(tInterval, z);
		return K * accumulationFactor;
	}
	
	/**
	 * Calculates the Interest of an amount as absolute value.
	 * @param K as capital
	 * @param z as interest
	 * @return returns the interest as absolute value.
	 */
	public static double calculateInterestOfAmount(double K, double z) {
		return K * BasicCalculator.interestToQuote(z);
	}

	/**
	 * Calculates the Accumulation Factor(german: Aufzinsungsfaktor)
	 * @param years to accumulate
	 * @param interest to accumulate
	 * @return the accumulation quote
	 */
	public static double accumulationFactorByYearsAndInterest(double years, double interest) {
		return Math.pow(1 + interestToQuote(interest), years);
	}
	
	/**
	 * Converts a value as quote, e.g. 20 (%) to 0.2
	 * @param interest
	 * @return
	 */
	public static double interestToQuote(double interest) {
		return interest / 100;
	}
	
	private BasicCalculator() {}
	
}
