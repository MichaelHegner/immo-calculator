package ch.hemisoft.immo.calc.business.utils;

public final class CreditCalculator {
	
	/**
	 * Calculates the interest in the given year p.a.
	 * @param amount to loan
	 * @param t the year to calculate
	 * @param runTime complete time loan running in years
	 * @param z as percent like 2.2%
	 * @return the interest to pay in the given year.
	 */
	public static double calculateInterestToPayAfterYear(double K, int t, double n, double z) {
		double q = 1 + (z / 100);
		double qn = getQPowN(n, q);
		double qt = getQPowN(t - 1, q);
		double result = K * ( (qn - qt) * (q - 1) ) / ( qn - 1 );
		return Math.max(result, 0);
	}
	
	//
	
	private static double getQPowN(double years, double q) {
		return Math.pow(q, years + 1);
	}
	
	private CreditCalculator() {}
}
