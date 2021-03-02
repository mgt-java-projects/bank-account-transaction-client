package com.mgt.app.bank.account.transaction.client.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataAccessException;
import org.springframework.data.mongodb.core.aggregation.ComparisonOperators.Eq;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mgt.app.bank.account.transaction.client.exception.BankAccountTransactionClientException;
import com.mgt.app.bank.account.transaction.event.common.dto.AccountTransactionDTO;
import com.mgt.app.bank.account.transaction.event.common.enums.TransactionType;
import com.mgt.app.bank.account.transaction.event.common.exception.AccountTransactionDataAccessException;
import com.mgt.app.bank.account.transaction.event.persist.repository.service.AccountTransactionRepositoryService;

/**
 * Test class to unit test all methods defined in
 * {@link AccountTransactionServiceImpl} class.
 * 
 * @author stami
 *
 */
@SpringBootTest(classes = { AccountTransactionServiceImpl.class, ModelMapper.class })
public class AccountTransactionServiceImplTest {

	/**
	 * Mock Instance of {@link AccountTransactionRepositoryService}
	 */
	@MockBean
	AccountTransactionRepositoryService repositoryServiceMock;

	/**
	 * Instance of {@link AccountTransactionServiceImpl}.
	 */
	@Autowired
	AccountTransactionServiceImpl accountTransactionServiceImpl;

	/**
	 * To test {@link AccountTransactionServiceImpl#findAccountBalance(String)
	 * method is giving proper account balance for the given account number.
	 * <b>Case:</b>Send a valid account number to get account balance. <br>
	 * <b>Expected:</b>Account balance should be return.
	 */
	@Test
	public void testFindAccountBalance() {
		when(repositoryServiceMock.findAllTransactionsForAccount(ArgumentMatchers.anyString()))
				.thenReturn(buildAccountTransListTestData("100200"));
		BigDecimal actual = accountTransactionServiceImpl.findAccountBalance("100200");
		assertEquals("100", actual);
	}

	/**
	 * To test {@link AccountTransactionServiceImpl#findAccountBalance(String)
	 * method is giving Exception. <b>Case:</b>Send a valid account number to get
	 * account balance. <br>
	 * <b>Expected:</b>BankAccountTransactionClientException thrown.
	 */
	@Test
	public void testFindAccountBalanceThrowsBankAccountTransactionClientException() {
		when(repositoryServiceMock.findAllTransactionsForAccount(ArgumentMatchers.anyString()))
				.thenThrow(new AccountTransactionDataAccessException("Db connection issue",null));
		assertThrows(BankAccountTransactionClientException.class,
		 ()->accountTransactionServiceImpl.findAccountBalance("100200"));
		verify(repositoryServiceMock, times(1)).findAllTransactionsForAccount(ArgumentMatchers.any());
	}

	/**
	 * Test data to use in test cases.
	 * 
	 * @param accountNumber
	 * @return List<AccountTransactionDTO>
	 */
	private List<AccountTransactionDTO> buildAccountTransListTestData(String accountNumber) {
		List<AccountTransactionDTO> dataList = new ArrayList<AccountTransactionDTO>();
		AccountTransactionDTO actDto = new AccountTransactionDTO();
		actDto.setAccountNumber(accountNumber);
		actDto.setAmount(new BigDecimal(200));
		actDto.setTransactionTs(new Date());
		actDto.setType(TransactionType.DEPOSIT);
		dataList.add(actDto);

		AccountTransactionDTO actDto2 = new AccountTransactionDTO();
		actDto2.setAccountNumber(accountNumber);
		actDto2.setAmount(new BigDecimal(100));
		actDto2.setTransactionTs(new Date());
		actDto2.setType(TransactionType.WITHDRAW);
		dataList.add(actDto2);

		return dataList;

	}
}
