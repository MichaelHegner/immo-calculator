package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.Property;

public interface CostPlanningService {
	List<CostPlanning> findAll(List<Property> properties);
	List<CostPlanning> findAll(Property property);
	List<CostPlanning> findAll(Property property, int yearNow);
	CostPlanning find(Long planningId);
	CostPlanning save(CostPlanning populated);
	void save(List<CostPlanning> costPlannings);
}
