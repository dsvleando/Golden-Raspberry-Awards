package br.com.outsera.gra.shared.exception;

public class CsvProcessingException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CsvProcessingException(String message) {
		super(message);
	}

	public CsvProcessingException(String message, Throwable cause) {
		super(message, cause);
	}
}
