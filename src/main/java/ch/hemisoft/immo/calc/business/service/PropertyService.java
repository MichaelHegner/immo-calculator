package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.Property;

public interface PropertyService {
	List<Property> findAll();
	List<Property> findAllBought();
	Property find(Long id);
	Property save(Property property);
}