package br.com.outsera.gra.shared.exception;

public class DataProcessingException extends BaseException {

	private static final long serialVersionUID = 1L;

	public DataProcessingException(String message) {
		super(message, "DATA_PROCESSING_ERROR");
	}

	public DataProcessingException(String message, Object... args) {
		super(message, "DATA_PROCESSING_ERROR", args);
	}

	public DataProcessingException(String message, Throwable cause) {
		super(message, cause, "DATA_PROCESSING_ERROR");
	}
}
