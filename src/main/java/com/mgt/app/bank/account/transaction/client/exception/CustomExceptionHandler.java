package com.mgt.app.bank.account.transaction.client.exception;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.ConversionNotSupportedException;
import org.springframework.beans.TypeMismatchException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.mgt.app.api.common.response.ApiFailureResponse;
import com.mgt.app.bank.account.transaction.event.common.exception.AccountTransactionDataAccessException;

/**
 * Exception handler class to capture exception thrown at Api operation and
 * respond proper error code to clients.
 * 
 * @author stami
 *
 */
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Logger instance.
	 */
	private static final Logger log = LoggerFactory.getLogger(CustomExceptionHandler.class);

	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Request not supported", 1012);
		log.error("HttpRequestMethodNotSupported",headers.getLocation());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleTypeMismatch(TypeMismatchException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Type mismatch", 1011);
		log.error("TypeMismatchException",headers.getLocation());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Message Not Readable", 1010);
		log.error("HttpRequestMethodNotSupported",headers.getLocation());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		ApiFailureResponse errorResponse = new ApiFailureResponse("MethodArgumentNotValid", 1007);
		log.error("MethodArgumentNotValid",headers.getLocation());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException ex, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		// TODO Auto-generated method stub
		ApiFailureResponse errorResponse = new ApiFailureResponse("NoHandlerFound", 1006);
		log.error("NoHandlerFound",ex.getRequestURL(),ex.getHttpMethod());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatus status, WebRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Internal server Error", 1005);
		log.error("Internal exception occured",ex,headers.getLocation());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	
	@ExceptionHandler(BankAccountTransactionClientException.class)
	protected ResponseEntity<ApiFailureResponse> handleBankAccountTransactionClientException(BankAccountTransactionClientException exp,HttpServletRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Bank account transaction API client exception:"+exp.getMessage(), 1004);
		log.error("Bank account transaction API client exception occured",exp,request.getRequestURL(),request.getRequestedSessionId());
		return new ResponseEntity<ApiFailureResponse>(errorResponse, HttpStatus.NOT_FOUND);

	}
	
	@ExceptionHandler(AccountTransactionDataAccessException.class)
	protected ResponseEntity<Object> handleAccountTransactionDataAccessException(AccountTransactionDataAccessException exp,HttpServletRequest request) {
		ApiFailureResponse errorResponse = new ApiFailureResponse("Internal server Error", 1003);
		log.error("DataAccessException occured",exp,request.getRequestURL(),request.getRequestedSessionId());
		return new ResponseEntity<Object>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}


}
