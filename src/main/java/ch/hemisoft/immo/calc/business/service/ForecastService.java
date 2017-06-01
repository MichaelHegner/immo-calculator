package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;

public interface ForecastService {
	List<Forecast> findAll(List<Property> properties);
	List<Forecast> findAll(Property property);
	void save(Property property);
	void save(List<Forecast> dbForcasts);
}
