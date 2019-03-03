package ch.hemisoft.immo.calc.business.service;

import ch.hemisoft.immo.domain.Credit;

public interface FinanceService {
	Credit swapCredit(Long creditId);
}