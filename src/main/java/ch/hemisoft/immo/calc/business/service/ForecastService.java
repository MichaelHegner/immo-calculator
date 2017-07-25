package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.calc.business.service.vo.ForecastVO;
import ch.hemisoft.immo.domain.Property;

public interface ForecastService {
	List<ForecastVO> findAll(List<Property> properties);
	List<ForecastVO> findAll(Property property);
}
