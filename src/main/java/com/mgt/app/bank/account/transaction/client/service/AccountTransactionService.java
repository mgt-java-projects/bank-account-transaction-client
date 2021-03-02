package com.mgt.app.bank.account.transaction.client.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import com.mgt.app.bank.account.transaction.client.model.AccountTransactionResponse;

/**
 * Provides interface to connect with account transaction events storage db and fetch records.
 * @author stami
 *
 */
public interface AccountTransactionService {
	public BigDecimal findAccountBalance(String accountNumber);
	public List<AccountTransactionResponse> findAllTransactionsForAccountWithFilterParams(String accountNumber,
			Optional<String> type, Optional<Date> startDate, Optional<Date> endDate);
}
