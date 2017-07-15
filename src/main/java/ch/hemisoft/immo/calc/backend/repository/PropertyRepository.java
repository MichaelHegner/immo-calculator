package ch.hemisoft.immo.calc.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.Property;
import ch.hemisoft.immo.domain.PropertyStatus;

public interface PropertyRepository extends JpaRepository<Property, Long> {
	List<Property> findAllByStatus(PropertyStatus status);
	List<Property> findAllByStatusIn(PropertyStatus... status);
}