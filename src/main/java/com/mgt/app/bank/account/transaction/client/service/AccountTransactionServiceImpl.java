package com.mgt.app.bank.account.transaction.client.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.mgt.app.bank.account.transaction.client.exception.BankAccountTransactionClientException;
import com.mgt.app.bank.account.transaction.client.model.AccountTransactionResponse;
import com.mgt.app.bank.account.transaction.event.common.dto.AccountTransactionDTO;
import com.mgt.app.bank.account.transaction.event.common.enums.TransactionType;
import com.mgt.app.bank.account.transaction.event.common.exception.AccountTransactionDataAccessException;
import com.mgt.app.bank.account.transaction.event.persist.repository.service.AccountTransactionRepositoryService;

/**
 * Service class to interact with {@link AccountTransactionRepository} and
 * process database records.
 * 
 * @author stami
 *
 */
@Service
public class AccountTransactionServiceImpl implements AccountTransactionService {
	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(AccountTransactionServiceImpl.class);

	/**
	 * Instance of {@link AccountTransactionRepositoryService}
	 */
	@Autowired
	AccountTransactionRepositoryService repositoryService;

	/**
	 * Instance of {@link ModelMapper}
	 */
	@Autowired
	ModelMapper modelMapper;

	/**
	 * Find the account balance from all transactions {@link AccountTransaction} for
	 * the given account number.
	 * 
	 * @param accountNumber
	 * @return balance
	 */
	public BigDecimal findAccountBalance(String accountNumber) {
		log.debug("In findAccountBalance...");
		List<AccountTransactionDTO> transactions = null;
		BigDecimal accountBalance = null;
		try {
			transactions = repositoryService.findAllTransactionsForAccount(accountNumber);
			if (!transactions.isEmpty()) {
				accountBalance = transactions.stream()
						.filter(transaction -> TransactionType.DEPOSIT.equals(transaction.getType()))
						.map(transaction -> transaction.getAmount()).reduce(BigDecimal::add).get();

				accountBalance = accountBalance.subtract(transactions.stream()
						.filter(transaction -> TransactionType.WITHDRAW.equals(transaction.getType()))
						.map(transaction -> transaction.getAmount()).reduce(BigDecimal::add).get());
			} else {
				throw new BankAccountTransactionClientException(
						"No records found for the given account: " + accountNumber,null);
			}
			log.debug("Out findAccountBalance...");

		} catch (AccountTransactionDataAccessException exp) {
			log.error("Exception occured while fetching accountBalance for account : " + accountNumber, exp);
			throw new BankAccountTransactionClientException(
					"Exception occured while fetching accountBalance for account: " + accountNumber, exp);
		}

		return accountBalance;
	}

	/**
	 * Find the list of transactions {@link AccountTransaction} for the given
	 * account number and type or date range filter.
	 * 
	 * @param accountNumber
	 * @param type
	 * @param startDate
	 * @param endDate
	 * @return List<AccountTransactionResponse>
	 */
	public List<AccountTransactionResponse> findAllTransactionsForAccountWithFilterParams(String accountNumber,
			Optional<String> type, Optional<Date> startDate, Optional<Date> endDate) {
		log.debug("In findAllTransactionsForAccountWithFilterParams...");
		List<AccountTransactionResponse> accountTransactionResponse = null;
		try {
			if (type.isPresent()) {
				if (startDate.isPresent() && endDate.isPresent()) {
					accountTransactionResponse = mapDtoToResponseModel(
							repositoryService.findAllTransactionsForAccountWithTypeAndDateRange(accountNumber,
									type.get(), startDate.get(), endDate.get()));
				} else {
					accountTransactionResponse = mapDtoToResponseModel(
							repositoryService.findAllTransactionsForAccountAndType(accountNumber, type.get()));
				}
			} else {
				accountTransactionResponse = mapDtoToResponseModel(
						repositoryService.findAllTransactionsForAccount(accountNumber));
			}
			log.debug("Out findAllTransactionsForAccountWithFilterParams...");

		} catch (AccountTransactionDataAccessException exp) {
			log.error("Exception occured while fetching transactions with filter params for account : " + accountNumber,
					exp);
			throw new BankAccountTransactionClientException(
					"Exception occured while fetching transactions with filter params for account : " + accountNumber,
					exp);
		}

		return accountTransactionResponse;
	}

	/**
	 * Mapping of data from {@link AccountTransactionDTO} DTO to
	 * {@link AccountTransactionResponse} model object
	 * 
	 * @param AccountTransactionDTO
	 * @return AccountTransactionResponse
	 */
	private AccountTransactionResponse mapDtoToResponseModel(final AccountTransactionDTO accountTransactionDTO) {
		log.debug("In mapDtoToResponseModel...");
		AccountTransactionResponse accountTransactionResponse = null;
		if (null != accountTransactionDTO) {
			accountTransactionResponse = modelMapper.map(accountTransactionDTO, AccountTransactionResponse.class);
		}
		// TODO: Additional property mapping, if required.
		log.debug("Out mapDtoToResponseModel...");
		return accountTransactionResponse;

	}

	/**
	 * Mapping of data from {@link AccountTransactionDTO} DTO list to
	 * {@link AccountTransactionResponse} model list object
	 * 
	 * @param List<AccountTransactionDTO>
	 * @return List<AccountTransactionResponse>
	 */
	private List<AccountTransactionResponse> mapDtoToResponseModel(
			final List<AccountTransactionDTO> accountTransactionDtos) {
		log.debug("In mapDtoToResponseModel...");
		List<AccountTransactionResponse> accountTransactionResponses = null;
		if (null != accountTransactionDtos) {
			accountTransactionResponses = accountTransactionDtos.stream().map(act -> mapDtoToResponseModel(act))
					.collect(Collectors.toList());
		}
		log.debug("Out mapDtoToResponseModel...");
		return accountTransactionResponses;

	}

}
