package br.com.outsera.gra.shared.exception;

public class CsvFileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public CsvFileNotFoundException(String message) {
		super(message);
	}

	public CsvFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}
