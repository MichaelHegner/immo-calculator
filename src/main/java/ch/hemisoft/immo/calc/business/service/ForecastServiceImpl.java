package ch.hemisoft.immo.calc.business.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.commons.CollectionUtils;
import ch.hemisoft.immo.calc.business.service.vo.ForecastVO;
import ch.hemisoft.immo.calc.business.utils.ForecastCalculator;
import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.Credit;
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
	@NonNull private CostPlanningService costPlanningService;
	@NonNull private ForecastConfigurationService forecastConfigurationService;
	
	@Override
	public List<ForecastVO> findAll(List<Property> properties) {

		// PREPARE ...
		List<ForecastVO> forecasts = new ArrayList<>(FORECAST_TERM);
		Map<String, ForecastConfiguration> forecastConfigurationMap = createForecastConfigurationMap();
		
		// POPULATE ...
		for(Property property : properties) {
			final boolean firstProperty = properties.indexOf(property) <= 0;
			
			
			final int yearToday = LocalDate.now().get(ChronoField.YEAR);
			List<ForecastVO> findAll = findAll(property);
			for(int i = 0; i < FORECAST_TERM; i++) {
				final int yearLoop = i + yearToday;
				
				final ForecastVO forecastAtI;
				if(firstProperty) {
					final BigDecimal netAssets = BigDecimalUtils.convert(property.getNetAssets());
					final String countryCode = property.getAddress().getCountryCode();
					final BigDecimal taxQuote = BigDecimalUtils.convert(forecastConfigurationMap.get(countryCode).getTaxQuote());
					final boolean german = "DE".equals(countryCode);
					forecastAtI = new ForecastVO(yearLoop, netAssets, taxQuote, german);
				} else {
					forecastAtI = forecasts.get(i);
				}
				
				forecastAtI.addIncomeBeforeCost(findAll.get(i).getIncomeBeforeCost());
				forecastAtI.addRunningCost(findAll.get(i).getRunningCost());
				forecastAtI.addSpecialCost(calculateSumSpecialCost(property, yearLoop));
				forecastAtI.addInterest(findAll.get(i).getInterest());
				forecastAtI.addDeprecation(findAll.get(i).getDeprecation());
				forecastAtI.addRedemption(findAll.get(i).getRedemption());
				
				if(firstProperty) {
					forecasts.add(forecastAtI);
				}
			}
		}
		
		return forecasts;
	}



	
	@Override
	public List<ForecastVO> findAll(Property property) {
		List<ForecastVO> forecasts = new ArrayList<>();
		String countryCode = property.getAddress().getCountryCode();
		ForecastConfiguration configuration = forecastConfigurationService.findByCountryCode(countryCode);
		
		final int yearToday = LocalDate.now().get(ChronoField.YEAR);
		for(int i = 0; i < FORECAST_TERM; i++) {
			final int yearLoop = i + yearToday;
			final BigDecimal netAssets = BigDecimalUtils.convert(property.getNetAssets());
			final BigDecimal taxQuote = BigDecimalUtils.convert(configuration.getTaxQuote());
			final boolean german = "DE".equals(countryCode);
			
			final ForecastVO forecast = new ForecastVO(yearLoop, netAssets, taxQuote, german);
			populateRental(property, configuration, forecast);
			populateRunningCost(property, configuration, forecast);
			populateSpecialCost(property, forecast, yearLoop);
			populateInterests(property, forecast);
			populateDeprecation(property, forecast, configuration);
			populateRedemptions(property, forecast);
			forecasts.add(forecast);
		}
		return forecasts;
	}




	//

	private Map<String, ForecastConfiguration> createForecastConfigurationMap() {
		return forecastConfigurationService.findAll().stream().collect(Collectors.toMap(ForecastConfiguration::getCountryCode, Function.identity()));
	}
	
	private BigDecimal calculateSumSpecialCost(Property property, int year) {
		List<CostPlanning> costPlannings = costPlanningService.findAll(property, year);
		BigDecimal sumSpecialCost = CollectionUtils.reduce(costPlannings, t -> t.getAmount());
		return sumSpecialCost;
	}

	//

	private void populateRental(Property property, ForecastConfiguration configuration, final ForecastVO forecast) {
		populateRental(property, forecast, configuration.getRentalIncrease(), configuration.getRentalIncreaseFrequence());
	}

	private void populateRental(Property property, ForecastVO forecast, double rentalIndex, int interval) {
		double rental = ForecastCalculator.calculateRental(property, forecast, rentalIndex, interval);
		forecast.addIncomeBeforeCost(BigDecimalUtils.convert(rental));
	}
	
	private void populateRunningCost(Property property, ForecastConfiguration configuration, final ForecastVO forecast) {
		populateRunningCost(property, forecast, configuration.getRunningCostIndex(), 1);
	}

	private void populateRunningCost(Property property, ForecastVO forecast, Double priceIndex, int interval) {
		double runningCost = ForecastCalculator.calculateRunningCost(property, forecast, priceIndex, interval);
		forecast.addRunningCost(BigDecimalUtils.convert(runningCost));
	}
	
	private void populateSpecialCost(Property property, ForecastVO forecast, int year) {
		BigDecimal sumSpecialCost = calculateSumSpecialCost(property, year);
		populateSpecialCost(forecast, sumSpecialCost);
	}

	private void populateSpecialCost(ForecastVO forecast, BigDecimal sumSpecialCost) {
		forecast.addSpecialCost(sumSpecialCost);
	}
	
	private void populateInterests(Property property, ForecastVO forecast) {
		for (Credit credit : property.getActiveCredits()) {
			populateInterest(property, credit, forecast);
		}
	}
	
	private void populateInterest(Property property, Credit credit, ForecastVO forecast) {
		double interest = ForecastCalculator.calculateInterest(property, credit, forecast);
		forecast.addInterest(BigDecimalUtils.convert(interest));
	}
	
	private void populateDeprecation(Property property, ForecastVO forecast, ForecastConfiguration configuration) {
		double deprecation = ForecastCalculator.calculateDeprecation(property, configuration);
		forecast.addDeprecation(BigDecimalUtils.convert(deprecation));
	}

	private void populateRedemptions(Property property, ForecastVO forecast) {
		for (Credit credit : property.getActiveCredits()) {
			populateRedemption(property, credit, forecast);
		}
	}
	
	private void populateRedemption(Property property, Credit credit, ForecastVO forecast) {
		double redemption = ForecastCalculator.calculateRedemption(property, credit, forecast);
		forecast.addRedemption(BigDecimalUtils.convert(redemption));
	}
}
