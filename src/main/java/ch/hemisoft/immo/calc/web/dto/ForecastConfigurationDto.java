package ch.hemisoft.immo.calc.web.dto;

import lombok.Data;

@Data
public class ForecastConfigurationDto {
	private String countryCode; 
	private Double taxQuote;
	private Double runningCostIndex;
	private Double deprecation;
	private Double rentalIncreaseAllTwoYears;
}
