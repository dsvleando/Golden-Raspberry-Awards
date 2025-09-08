package br.com.outsera.gra.presentation.exception;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import br.com.outsera.gra.presentation.dto.response.ErrorResponse;
import br.com.outsera.gra.shared.exception.BaseException;
import br.com.outsera.gra.shared.exception.BusinessException;
import br.com.outsera.gra.shared.exception.DataProcessingException;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.exception.ValidationException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex,
			HttpServletRequest request) {

		log.warn("Resource not found: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode()).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
	}

	@ExceptionHandler(ValidationException.class)
	public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex, HttpServletRequest request) {

		log.warn("Validation error: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode()).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(BusinessException.class)
	public ResponseEntity<ErrorResponse> handleBusinessException(BusinessException ex, HttpServletRequest request) {

		log.warn("Business error: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode()).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(error);
	}

	@ExceptionHandler(DataProcessingException.class)
	public ResponseEntity<ErrorResponse> handleDataProcessingException(DataProcessingException ex,
			HttpServletRequest request) {

		log.error("Data processing error: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode()).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex,
			HttpServletRequest request) {

		log.warn("Validation error: {}", ex.getMessage());

		List<ErrorResponse.FieldError> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
				.map(this::mapFieldError).collect(Collectors.toList());

		ErrorResponse error = ErrorResponse.builder().errorCode("VALIDATION_ERROR")
				.message("Erro de validação nos dados enviados").timestamp(LocalDateTime.now())
				.path(request.getRequestURI()).fieldErrors(fieldErrors).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex, HttpServletRequest request) {

		log.warn("Application error: {}", ex.getMessage());

		ErrorResponse error = ErrorResponse.builder().errorCode(ex.getErrorCode()).message(ex.getMessage())
				.timestamp(LocalDateTime.now()).path(request.getRequestURI()).build();

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, HttpServletRequest request) {

		log.error("Unexpected error: {}", ex.getMessage(), ex);

		ErrorResponse error = ErrorResponse.builder().errorCode("INTERNAL_SERVER_ERROR")
				.message("Erro interno do servidor").timestamp(LocalDateTime.now()).path(request.getRequestURI())
				.build();

		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
	}

	private ErrorResponse.FieldError mapFieldError(FieldError fieldError) {
		return ErrorResponse.FieldError.builder().field(fieldError.getField()).message(fieldError.getDefaultMessage())
				.rejectedValue(fieldError.getRejectedValue()).build();
	}
}
