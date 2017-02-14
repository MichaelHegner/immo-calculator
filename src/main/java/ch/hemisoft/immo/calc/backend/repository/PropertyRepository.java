package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.Property;

public interface PropertyRepository extends JpaRepository<Property, Long> {}