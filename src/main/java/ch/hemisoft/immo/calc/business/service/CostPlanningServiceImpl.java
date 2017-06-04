package ch.hemisoft.immo.calc.business.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.CostPlanningRepository;
import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.Property;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class CostPlanningServiceImpl implements CostPlanningService {
	@NonNull private PropertyService propertyService;
	@NonNull private CostPlanningRepository costPlanningRepository;
	
	@Override
	public List<CostPlanning> findAll(List<Property> properties) {
		return costPlanningRepository.findAllByPropertyInOrderByDateYearAsc(properties);
	}
	
	@Override
	public List<CostPlanning> findAll(Property property) {
		return costPlanningRepository.findAllByPropertyOrderByDateYearAsc(property);
	}

	@Override
	public void save(List<CostPlanning> plannings) {
		plannings.forEach(costPlanningRepository::save);
	}
}
