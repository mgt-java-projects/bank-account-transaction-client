package com.mgt.app.bank.account.transaction.client.controller;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mgt.app.api.common.response.ApiSuccesResponse;
import com.mgt.app.bank.account.transaction.client.model.AccountTransactionResponse;
import com.mgt.app.bank.account.transaction.client.service.AccountTransactionService;
import com.mgt.app.bank.account.transaction.client.service.AccountTransactionServiceImpl;

import io.swagger.annotations.ApiParam;

/**
 * Resource class provides features to get records from account transaction data
 * storage.
 * 
 * @author stami
 *
 */
@RestController
@RequestMapping("/api/transaction/account")
public class AccountTransactionResource {

	/**
	 * Instance of {@link AccountTransactionServiceImpl}.
	 */
	@Autowired
	AccountTransactionService accountTransactionService;

	/**
	 * Fetch account balance when only account number is given, or returns list of
	 * transaction based on given input filter criteria.
	 * 
	 * @param accountNumber
	 * @param creationDateTimeFrom
	 * @param creationDateTimeTo
	 * @param type
	 * @return
	 */
	@GetMapping("/{accountNumber}")
	public ResponseEntity<Object> getTransactions(
			@PathVariable(value = "accountNumber", required = true) final String accountNumber,
			@ApiParam("creationDateTimeFrom") @RequestParam(value = "creationDateTimeFrom", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<Date> creationDateTimeFrom,
			@RequestParam(value = "creationDateTimeTo", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Optional<Date> creationDateTimeTo,
			@RequestParam(value = "type", required = false) Optional<String> type) {
		ResponseEntity<Object> response;
		if (type.isPresent() || (creationDateTimeFrom.isPresent() && creationDateTimeTo.isPresent())) {
			List<AccountTransactionResponse> transactions = accountTransactionService
					.findAllTransactionsForAccountWithFilterParams(accountNumber, type, creationDateTimeFrom,
							creationDateTimeTo);
			ApiSuccesResponse<List<AccountTransactionResponse>> apiSuccesResponse=new ApiSuccesResponse<List<AccountTransactionResponse>>("Transaction list retrieved", transactions);
			response = new ResponseEntity<>(apiSuccesResponse,HttpStatus.OK);
		} else {
			BigDecimal accountBalance = accountTransactionService.findAccountBalance(accountNumber);
			ApiSuccesResponse<BigDecimal> apiSuccesResponse=new ApiSuccesResponse<BigDecimal>("balance retrieved", accountBalance);
			response = new ResponseEntity<>(apiSuccesResponse, HttpStatus.OK);
		}
		return response;
	}

}
