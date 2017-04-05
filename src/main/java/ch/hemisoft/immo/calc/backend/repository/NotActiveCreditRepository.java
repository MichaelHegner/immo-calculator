package ch.hemisoft.immo.calc.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import ch.hemisoft.immo.domain.NotActiveCredit;

public interface NotActiveCreditRepository extends JpaRepository<NotActiveCredit, Long> {}