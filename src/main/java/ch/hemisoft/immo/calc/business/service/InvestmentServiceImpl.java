package ch.hemisoft.immo.calc.business.service;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import ch.hemisoft.immo.calc.backend.repository.CreditRepository;
import ch.hemisoft.immo.domain.Credit;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class InvestmentServiceImpl implements InvestmentService {
	@NonNull private final CreditRepository repository;

	@Override
	public Credit swapCredit(Long creditId) {
		Credit credit = repository.findOne(creditId);
		credit.setActive(!credit.isActive());
		return credit;
	}

}