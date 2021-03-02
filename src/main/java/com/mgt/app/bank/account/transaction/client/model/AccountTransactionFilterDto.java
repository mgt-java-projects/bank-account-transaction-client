package com.mgt.app.bank.account.transaction.client.model;

import lombok.Data;

@Data
public class AccountTransactionFilterDto {
	private String accountNumber;
	private String creationDateTimeFrom;
	private String creationDateTimeTo;
	private String type;
}
