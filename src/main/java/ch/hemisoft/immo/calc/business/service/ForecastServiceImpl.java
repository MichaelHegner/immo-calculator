package ch.hemisoft.immo.calc.business.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.ForecastRepository;
import ch.hemisoft.immo.calc.business.utils.CreditCalculator;
import ch.hemisoft.immo.domain.Credit;
import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.ForecastConfiguration;
import ch.hemisoft.immo.domain.Property;
import ch.hemisoft.immo.utils.BigDecimalUtils;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ForecastServiceImpl implements ForecastService {
	private static final int FORECAST_TERM = 10;
	
	@NonNull private PropertyService propertyService;
	@NonNull private ForecastConfigurationService forecastConfigurationService;
	@NonNull private ForecastRepository forecastRepository;
	

	@Override
	public List<Forecast> findAll(List<Property> properties) {
		properties.forEach(this::save); // TODO SOC

		// PREPARE ...
		List<Forecast> forecasts = new ArrayList<>(FORECAST_TERM);
		for(int i = 0; i < FORECAST_TERM; i++) forecasts.add(new Forecast()); // INITIAL LIST WITH EMTPY FORCAST FOR REUSING
		Map<String, ForecastConfiguration> forecastConfigurationMap = 
				forecastConfigurationService.findAll().stream().collect(Collectors.toMap(ForecastConfiguration::getCountryCode, Function.identity()));
		
		// POPULATE ...
		for(Property property : properties) {
			List<Forecast> findAll = findAll(property);
			for(int i = 0; i < FORECAST_TERM; i++) {
				final Forecast forecastAtI = forecasts.get(i);
				forecastAtI.setYear(i + 1);
				forecastAtI.setProperty(property); // TEMPORARY SET PROPERTY FOR CALCULATION
				forecastAtI.setConfiguration(forecastConfigurationMap.get(forecastAtI.getProperty().getAddress().getCountryCode()));
				forecastAtI.setIncomeBeforeCost(forecastAtI.getIncomeBeforeCost().add(findAll.get(i).getIncomeBeforeCost()));
				forecastAtI.setRunningCost(forecastAtI.getRunningCost().add(findAll.get(i).getRunningCost()));
				forecastAtI.setSpecialCost(forecastAtI.getSpecialCost().add(findAll.get(i).getSpecialCost()));
				forecastAtI.setInterest(forecastAtI.getInterest().add(findAll.get(i).getInterest()));
				forecastAtI.setDeprecation(forecastAtI.getDeprecation().add(findAll.get(i).getDeprecation()));
				forecastAtI.setRedemption(forecastAtI.getRedemption().add(findAll.get(i).getRedemption()));
			}
		}
		
		return forecasts;
	}
	
	@Override
	public List<Forecast> findAll(Property property) {
		save(property); // TODO SOC
		return forecastRepository.findAllByPropertyOrderByYearAsc(property);
	}

	@Override
	public void save(Property property) {
		String countryCode = property.getAddress().getCountryCode();
		ForecastConfiguration configuration = forecastConfigurationService.findByCountryCode(countryCode);
		
		for(int i = 0; i < FORECAST_TERM; i++) {
			Forecast forecast = forecastRepository.findByYearAndProperty(i, property);
			if(null == forecast) {
				forecast = new Forecast(property, configuration, i);
				forecastRepository.save(forecast);
			}
			
			populateRental(property, forecast, configuration.getRentalIncreaseAllTwoYears(), 2);
			populateRunningCost(property, forecast, configuration.getRunningCostIndex(), 1);
			populateInterest(property, forecast, property.getSelectedCredit().getInterestRateNominalInPercent().doubleValue());
			populateDeprecation(property, forecast);
			populateRedemption(property, forecast, property.getSelectedCredit().getInterestRateNominalInPercent().doubleValue());
		}
	}

	//

	private void populateRental(Property property, Forecast forecast, double percentalIncrease, int increaseFrequence) {
		double value = property.getRentalNet().doubleValue();
		double i = percentalIncrease / 100; 
		long currentYear = forecast.getYear();
		double q = 1 + i;
		double t = currentYear % increaseFrequence == 0 ? currentYear :  currentYear - currentYear % increaseFrequence;
		double qt = Math.pow(q, t);
		double result = value * qt;
		forecast.setIncomeBeforeCost(BigDecimalUtils.convert(result));
	}

	private void populateRunningCost(Property property, Forecast forecast, Double percentalIncrease, int increaseFrequence) {
		double value = property.getTotalManagementCost().doubleValue();
		double i = percentalIncrease / 100;
		long currentYear = forecast.getYear();
		double q = 1 + i;
		double t = currentYear % increaseFrequence == 0 ? currentYear :  currentYear - currentYear % increaseFrequence;
		double qt = Math.pow(q, t);
		double result = value * qt;
		forecast.setRunningCost(BigDecimalUtils.convert(result));
	}
	
	private void populateInterest(Property property, Forecast forecast, Double percental) {
		double result = calculateInterest(property, forecast, percental);
		forecast.setInterest(BigDecimalUtils.convert(result));
	}
	
	private void populateDeprecation(Property property, Forecast forecast) {
		double result = calculateDeprecation(property, forecast);
		forecast.setDeprecation(BigDecimalUtils.convert(result));
	}
	
	private void populateRedemption(Property property, Forecast forecast, double doubleValue) {
		Credit selectedCredit = property.getSelectedCredit();
		double financialNeedsTotal = property.getFinancialNeedsTotal().doubleValue();
		double interest = selectedCredit.getInterestRateNominalInPercent().doubleValue();
		double redemption = selectedCredit.getRedemptionAtBeginInPercent().doubleValue();
		double specialRedemption = selectedCredit.getSpecialRedemptionEachYearInPercent().doubleValue();
		double annuity = CreditCalculator.calculateAnnuity(financialNeedsTotal, interest, redemption + specialRedemption);
		double calculateInterest = calculateInterest(property, forecast, interest);
		forecast.setRedemption(BigDecimalUtils.convert(annuity - calculateInterest));
	}

	private double calculateInterest(Property property, Forecast forecast, Double interestInPercent) {
		double value = property.getPurchasePrice().doubleValue();
		int currentYear = forecast.getYear() + 1;

		Credit selectedCredit = property.getSelectedCredit();
		double zRedemption = selectedCredit.getRedemptionAtBeginInPercent().doubleValue();
		double zSpecialRedemption = selectedCredit.getSpecialRedemptionEachYearInPercent().doubleValue();
		return CreditCalculator.calculateInterestInYear(value, interestInPercent, zRedemption, zSpecialRedemption, currentYear);
	}
	
	private double calculateDeprecation(Property property, Forecast forecast) {
		double value = property.getPurchasePrice().doubleValue();
		double i = forecast.getConfiguration().getDeprecation() / 100;
		return value * i;
	}

}
