package br.com.outsera.gra.shared.exception;

public class BusinessException extends BaseException {

	private static final long serialVersionUID = 1L;

	public BusinessException(String message) {
		super(message, "BUSINESS_ERROR");
	}

	public BusinessException(String message, Object... args) {
		super(message, "BUSINESS_ERROR", args);
	}

	public BusinessException(String message, Throwable cause) {
		super(message, cause, "BUSINESS_ERROR");
	}
}
