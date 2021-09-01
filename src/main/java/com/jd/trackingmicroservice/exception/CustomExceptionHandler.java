package com.jd.trackingmicroservice.exception;

import org.hibernate.NonUniqueObjectException;
import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.exception.SQLGrammarException;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

	/**
	 * Handle HttpMessageNotReadableException. Happens when request JSON is
	 * malformed.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiErrorVO object
	 */
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = "Malformed JSON request";
		return buildResponseEntity(new ApiErrorVO(HttpStatus.BAD_REQUEST, error, ex));
	}

	/**
	 * Handle MissingServletRequestParameterException. Triggered when a 'required'
	 * request parameter is missing.
	 *
	 * @param ex      MissingServletRequestParameterException
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiErrorVO object
	 */
	@Override
	protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {
		String error = ex.getParameterName() + " parameter is missing";
		return buildResponseEntity(new ApiErrorVO(status, error, ex));
	}

	/**
	 * Handle MethodArgumentNotValidException. Triggered when an object fails @Valid
	 * validation.
	 *
	 * @param ex      the MethodArgumentNotValidException that is thrown when @Valid
	 *                validation fails
	 * @param headers HttpHeaders
	 * @param status  HttpStatus
	 * @param request WebRequest
	 * @return the ApiErrorVO object
	 */
	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
			HttpHeaders headers, HttpStatus status, WebRequest request) {

		// LOGGER.info("Validation error list : "+validationList);
		ApiErrorVO apiErrorVO = new ApiErrorVO(status);
		apiErrorVO.setMessage("Validation error");
		apiErrorVO.addValidationErrors(ex.getBindingResult().getFieldErrors());
		apiErrorVO.addValidationError(ex.getBindingResult().getGlobalErrors());

		return buildResponseEntity(apiErrorVO);
	}

	/**
	 * Handle IllegalArgumentException and IllegalStateException
	 *
	 * @param ex the RuntimeException
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(value = { IllegalArgumentException.class, IllegalStateException.class })
	protected ResponseEntity<Object> handleConflict(RuntimeException ex) {
		String error = ex.getMessage();
		return buildResponseEntity(new ApiErrorVO(HttpStatus.CONFLICT, error, ex));
	}

	/**
	 * Handles javax.validation.ConstraintViolationException. Thrown when @Validated
	 * fails.
	 *
	 * @param ex the ConstraintViolationException
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(javax.validation.ConstraintViolationException.class)
	protected ResponseEntity<Object> handleConstraintViolation(javax.validation.ConstraintViolationException ex) {
		ApiErrorVO ApiErrorVO = new ApiErrorVO(HttpStatus.BAD_REQUEST);
		ApiErrorVO.setMessage("Validation error");
		ApiErrorVO.addValidationErrors(ex.getConstraintViolations());
		return buildResponseEntity(ApiErrorVO);
	}

	/**
	 * Handle DataIntegrityViolationException, inspects the cause for different DB
	 * causes.
	 *
	 * @param ex the DataIntegrityViolationException
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(DataIntegrityViolationException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(DataIntegrityViolationException ex) {
		if (ex.getCause() instanceof ConstraintViolationException) {
			return buildResponseEntity(new ApiErrorVO(HttpStatus.CONFLICT, "Database error", ex.getCause()));
		}
		return buildResponseEntity(new ApiErrorVO(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle SQLGrammarException, inspects the cause for different DB causes.
	 *
	 * @param ex the SQLGrammarException
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(SQLGrammarException.class)
	protected ResponseEntity<Object> handleDataIntegrityViolation(SQLGrammarException ex) {

		return buildResponseEntity(new ApiErrorVO(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}

	/**
	 * Handle NonUniqueObjectException
	 *
	 * @param ex the NonUniqueObjectException
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(NonUniqueObjectException.class)
	protected ResponseEntity<Object> handleNonUniqueObjectException(NonUniqueObjectException ex) {

		return buildResponseEntity(new ApiErrorVO(HttpStatus.INTERNAL_SERVER_ERROR, ex));
	}
	
	/**
	 * Handle NullPointerException
	 *
	 * @param ex the Exception
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<Object> handleAllException(NullPointerException ex, WebRequest request) {
		ApiErrorVO apiError = new ApiErrorVO(
			      HttpStatus.NOT_FOUND, ex.getMessage(), "NullPointerException occurred");
		return new ResponseEntity<Object>(
			      apiError, new HttpHeaders(), apiError.getStatus());
	}

	/**
	 * Handle Exception
	 *
	 * @param ex the Exception
	 * @return the ApiErrorVO object
	 */
	@ExceptionHandler(Exception.class)
	protected ResponseEntity<Object> handleAllException(Exception ex, WebRequest request) {
		ApiErrorVO apiError = new ApiErrorVO(
			      HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "error occurred");
		return new ResponseEntity<Object>(
			      apiError, new HttpHeaders(), apiError.getStatus());
	}

	private ResponseEntity<Object> buildResponseEntity(ApiErrorVO ApiErrorVO) {
		return new ResponseEntity<>(ApiErrorVO, ApiErrorVO.getStatus());
	}
}
