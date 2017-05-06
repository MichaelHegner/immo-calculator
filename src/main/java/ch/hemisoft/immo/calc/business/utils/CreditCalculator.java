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
	
	public static double calculateInterestInYear(double K, double zInterest, double zRedemption, double zSpecialRedemption, int afterNumberOfYears) {
		return calculateRestLoanInYear(K, zInterest, zRedemption, zSpecialRedemption, afterNumberOfYears) * quote(zInterest); 
	}
	
	public static double calculateRestLoanInYear(double K, double zInterest, double zRedemption, double zSpecialRedemption, int afterNumberOfYears) {
		double annuity = calculateAnnuity(K, zInterest, zRedemption);
		double T1 = annuity + quote(zSpecialRedemption);
		double q = 1 + quote(zInterest);
		double qPowN = getQPowN(afterNumberOfYears - 1, q);
		double result = K - ( T1 * (qPowN - 1) ) / ( q - 1 );
		return Math.max(result, 0);
	}

	public static double calculateAnnuity(double K, double zInterest, double zRedemption) {
		return K * quote(zInterest + zRedemption);
	}
	
	//
	
	private static double getQPowN(double years, double q) {
		return Math.pow(q, years + 1);
	}
	
	private static double quote(double value) {
		return value / 100;
	}
	
	private CreditCalculator() {}
}
