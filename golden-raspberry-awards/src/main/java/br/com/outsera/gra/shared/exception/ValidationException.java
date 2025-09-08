package br.com.outsera.gra.shared.exception;

public class ValidationException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ValidationException(String message) {
		super(message, "VALIDATION_ERROR");
	}

}
