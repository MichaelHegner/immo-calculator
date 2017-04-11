package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.ForecastConfiguration;

public interface ForecastConfigurationService {
	List<ForecastConfiguration> findAll();
	ForecastConfiguration findByCountryCode(String countryCode);
}
