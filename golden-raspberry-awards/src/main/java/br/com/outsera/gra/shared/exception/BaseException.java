package br.com.outsera.gra.shared.exception;

public abstract class BaseException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private final String errorCode;
	private final Object[] args;

	protected BaseException(String message, String errorCode, Object... args) {
		super(message);
		this.errorCode = errorCode;
		this.args = args;
	}

	protected BaseException(String message, Throwable cause, String errorCode, Object... args) {
		super(message, cause);
		this.errorCode = errorCode;
		this.args = args;
	}

	public String getErrorCode() {
		return errorCode;
	}

	public Object[] getArgs() {
		return args;
	}
}
