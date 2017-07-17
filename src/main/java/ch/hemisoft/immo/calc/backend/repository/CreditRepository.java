package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.Credit;

public interface CreditRepository extends JpaRepository<Credit, Long> {}