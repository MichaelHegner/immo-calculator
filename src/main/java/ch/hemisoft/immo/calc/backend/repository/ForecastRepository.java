package ch.hemisoft.immo.calc.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.Forecast;
import ch.hemisoft.immo.domain.Property;

public interface ForecastRepository extends JpaRepository<Forecast, Long> {
	List<Forecast> findAllByPropertyInOrderByYearAsc(List<Property> properties);
	List<Forecast> findAllByPropertyOrderByYearAsc(Property property);
	Forecast findByYearAndProperty(int i, Property property);
}