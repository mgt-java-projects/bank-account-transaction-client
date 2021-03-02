package com.mgt.app.bank.account.transaction.client.model;

import java.math.BigDecimal;
import java.util.Date;

import com.mgt.app.bank.account.transaction.event.common.enums.TransactionType;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("Account Transaction Response Model")
public class AccountTransactionResponse {

	@ApiModelProperty("Account number")
	private String accountNumber;

	@ApiModelProperty("Transaction time stamp in ISO")
	private Date transactionTs;

	@ApiModelProperty("Transaction type")
	private TransactionType type;

	@ApiModelProperty("Transaction Amount")
	private BigDecimal amount;
}
