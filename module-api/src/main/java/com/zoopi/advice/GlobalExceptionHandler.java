package com.zoopi.advice;

import static com.zoopi.exception.response.ErrorCode.*;

import javax.validation.ConstraintViolationException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import com.zoopi.exception.BusinessException;
import com.zoopi.exception.response.ErrorCode;
import com.zoopi.exception.response.ErrorResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	/**
	 * <b>@Validated</b>에 의해 발생하는 <b>Bean Validation</b> 예외 처리<br>
	 * @see <a href="https://medium.com/javarevisited/are-you-using-valid-and-validated-annotations-wrong-b4a35ac1bca4">@Valid vs @Validated</a>
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleConstraintViolationException(ConstraintViolationException e) {
		final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getConstraintViolations());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * <b>@Valid</b>에 의해 발생하는 <b>Bean Validation</b> 예외 처리
	 * @see <a href="https://medium.com/javarevisited/are-you-using-valid-and-validated-annotations-wrong-b4a35ac1bca4">@Valid vs @Validated</a>
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * <b>@ModelAttribute</b> 바인딩 실패 예외 처리
	 * @see <a href="https://docs.spring.io/spring/docs/current/spring-framework-reference/web.html#mvc-ann-modelattrib-method-args">@ModelAttribute</a>
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleBindException(BindException e) {
		final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, e.getBindingResult());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * <b>@RequestParam</b> or <b>@RequestPart</b> 필수 파라미터가 결여된 경우 예외 처리
	 */
	@ExceptionHandler({MissingServletRequestParameterException.class, MissingServletRequestPartException.class})
	protected ResponseEntity<ErrorResponse> handleMissingServletRequestParameterException(Exception e) {
		final ErrorResponse response = ErrorResponse.of(INPUT_VALUE_INVALID, getRequestParam(e));
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	private String getRequestParam(Exception e) {
		if (e instanceof MissingServletRequestParameterException) {
			return ((MissingServletRequestParameterException)e).getParameterName();
		} else {
			return ((MissingServletRequestPartException)e).getRequestPartName();
		}
	}

	/**
	 * Controller method argument <b>타입</b>이 일치하지 않을 경우 예외 처리
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleMethodArgumentTypeMismatchException(
		MethodArgumentTypeMismatchException e) {
		final ErrorResponse response = ErrorResponse.of(e);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * <b>@RequestBody</b> 형식과 불일치하거나, <b>JSON</b> 형식에 맞지 않을 경우 예외 처리
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		final ErrorResponse response = ErrorResponse.of(HTTP_MESSAGE_NOT_READABLE);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 지원하지 않는 <b>HTTP method</b> 호출할 경우 예외 처리
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		final ErrorResponse response = ErrorResponse.of(METHOD_NOT_ALLOWED);
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * <b>Business logic</b>에서 발생하는 모든 예외 처리
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleBusinessException(BusinessException e) {
		final ErrorCode errorCode = e.getErrorCode();
		final ErrorResponse response = ErrorResponse.of(errorCode, e.getErrors());
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}

	/**
	 * 위에 해당하지 않는 모든 예외 처리
	 */
	@ExceptionHandler
	protected ResponseEntity<ErrorResponse> handleException(Exception e) {
		final ErrorResponse response = ErrorResponse.of(INTERNAL_SERVER_ERROR);
		return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
