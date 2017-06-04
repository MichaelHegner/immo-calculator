package ch.hemisoft.immo.calc.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.CostPlanning;
import ch.hemisoft.immo.domain.Property;

public interface CostPlanningRepository extends JpaRepository<CostPlanning, Long> {
	List<CostPlanning> findAllByPropertyInOrderByDateYearAsc(List<Property> properties);
	List<CostPlanning> findAllByPropertyOrderByDateYearAsc(Property property);
	CostPlanning findByDateYearAndProperty(int i, Property property);
}