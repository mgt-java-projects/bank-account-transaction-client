package com.mgt.app.bank.account.transaction.client.exception;

/**
 * Exception thrown by account transaction persist module during data base
 * interaction.
 * 
 * @author stami
 *
 */
public class BankAccountTransactionClientException extends RuntimeException {

	/**
	 * serialVersionUID as recommended.
	 */
	private static final long serialVersionUID = -8552861422905621801L;

	/**
	 * Constructor.
	 * 
	 * @param message
	 * @param exception
	 */
	public BankAccountTransactionClientException(final String message, final Throwable exception) {
		super(message, exception);

	}
}
