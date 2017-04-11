package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.ForecastConfiguration;

public interface ForecastConfigurationRepository extends JpaRepository<ForecastConfiguration, Long> {
	ForecastConfiguration findByCountryCode(String countryCode);
}