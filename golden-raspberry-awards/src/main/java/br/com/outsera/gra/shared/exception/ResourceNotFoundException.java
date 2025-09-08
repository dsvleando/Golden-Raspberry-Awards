package br.com.outsera.gra.shared.exception;

public class ResourceNotFoundException extends BaseException {

	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(String resource, Object id) {
		super(String.format("%s não encontrado com ID: %s", resource, id), "RESOURCE_NOT_FOUND", resource, id);
	}
}