package ch.hemisoft.immo.calc.business.utils;

import java.time.LocalDate;
import java.time.temporal.ChronoField;

import ch.hemisoft.immo.calc.business.service.vo.ForecastVO;
import ch.hemisoft.immo.domain.Credit;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import ch.hemisoft.immo.domain.Property;
import net.hemisoft.financial.calculator.library.utils.AnnuityCalculator;
import net.hemisoft.financial.calculator.library.utils.BasicCalculator;

public final class ForecastCalculator {
	
	
	public static double calculateRental(Property property, ForecastVO forecast, Double rentalIndex, int interval) {
		double K = (null == property.getRentalNet()) ? 0.0 : property.getRentalNet().doubleValue();
		int yearDiff = ForecastCalculator.calculatePropertyForecastOfYear(property, forecast);
		return BasicCalculator.calculateInterestOfAmountWithInterval(K, rentalIndex, yearDiff, interval);
	}	
	
	public static double calculateRunningCost(Property property, ForecastVO forecast, Double percentalIncrease, int increaseFrequence) {
		double K = property.getTotalManagementCost().doubleValue();
		int yearDiff = ForecastCalculator.calculatePropertyForecastOfYear(property, forecast);
		return BasicCalculator.calculateInterestOfAmountWithInterval(K, percentalIncrease, yearDiff, increaseFrequence);
	}

	//
	
	public static double calculateRedemption(Property property, Credit credit, ForecastVO forecast) {
		int forecastYear = forecast.getYear();
		LocalDate purchaseDate = property.getPurchaseDate();
        int yearPurchaseProperty = purchaseDate != null ? purchaseDate.get(ChronoField.YEAR) : LocalDate.now().get(ChronoField.YEAR);
		int termSelectedCredit = credit.getTerm().intValue();
		
		if (forecastYear - yearPurchaseProperty <= termSelectedCredit) {
			int yearDiff = calculatePropertyForecastAtEndOfYear(property, forecast);
			return calculateRedemption(property, credit, forecast, yearDiff);
		} else {
			return 0.0;
		}
	}
	
	public static double calculateRedemption(Property property, Credit credit, ForecastVO forecast, int year) {
		double financialNeedsTotal = credit.getCapital().doubleValue();
		double zRedemptionAtBegin = credit.getRedemptionAtBeginInPercent().doubleValue();
		double zSpecialRedemption = credit.getSpecialRedemptionEachYearInPercent().doubleValue();
		double zInterest = credit.getInterestRateNominalInPercent().doubleValue();
		return AnnuityCalculator.calculateRedemptionAfterYear(financialNeedsTotal, zInterest, zRedemptionAtBegin, zSpecialRedemption, year);
	}
	
	//
	
	
	public static double calculateInterest(Property property, Credit credit, ForecastVO forecast) {
		int yearDiff = ForecastCalculator.calculatePropertyForecastAtEndOfYear(property, forecast);
		return calculateInterest(credit, yearDiff);
	}
	
	public static double calculateInterest(Credit credit, int year) {
		double K = credit.getCapital().doubleValue();
		Double zInterest = credit.getInterestRateNominalInPercent().doubleValue();
		double zRedemption = credit.getRedemptionAtBeginInPercent().doubleValue();
		double zSpecialRedemption = credit.getSpecialRedemptionEachYearInPercent().doubleValue();
		return AnnuityCalculator.calculateInterestInYear(K, zInterest, zRedemption, zSpecialRedemption, year);
	}

	/**
	 * Calculates the Deprecation of the given property with the depreacation quote of the configuration.
	 * @param property to deprecate
	 * @param forecastConfiguration with information about deprecation quote.
	 * @return the deprecation of the given property.
	 */
	public static double calculateDeprecation(Property property, ForecastConfiguration forecastConfiguration) {
		double K = property.getPurchasePrice().doubleValue();
		double z = forecastConfiguration.getDeprecation();
		return BasicCalculator.calculateInterestOfAmount(K, z);
	}

	/**
	 * Calculates the year to forecast by the difference of purchase at the end of year (if given, else current date) 
	 * of the property and the year to forecast. 
	 * @param property to calculate the start year.
	 * @param forecast to get the year to forcast.
	 * @return the number of the year between property purchase date (or now) and to forecast year.
	 */
	public static int calculatePropertyForecastAtEndOfYear(Property property, ForecastVO forecast) {
		return calculatePropertyForecastOfYear(property, forecast) + 1;
	}

	/**
	 * Calculates the year to forecast by the difference of purchaseof year (if given, else current date) 
	 * of the property and the year to forecast. 
	 * @param property to calculate the start year.
	 * @param forecast to get the year to forcast.
	 * @return the number of the year between property purchase date (or now) and to forecast year.
	 */
	public static int calculatePropertyForecastOfYear(Property property, ForecastVO forecast) {
		int forecastInYear = forecast.getYear();

		int propertyPurchaseYear;
		if (null == property || null == property.getPurchaseDate()) {
			propertyPurchaseYear = LocalDate.now().get(ChronoField.YEAR);
		} else {
			propertyPurchaseYear = property.getPurchaseDate().get(ChronoField.YEAR);
		}
		
		return forecastInYear - propertyPurchaseYear;
	}
	
	private ForecastCalculator(){}


}
