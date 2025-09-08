package br.com.outsera.gra.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ErrorResponse {

	private String errorCode;
	private String message;
	private LocalDateTime timestamp;
	private String path;
	private List<FieldError> fieldErrors;

	@Data
	@Builder
	public static class FieldError {
		private String field;
		private String message;
		private Object rejectedValue;
	}
}
