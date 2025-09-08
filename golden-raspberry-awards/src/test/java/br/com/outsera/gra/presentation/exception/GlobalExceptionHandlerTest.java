package br.com.outsera.gra.presentation.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.outsera.gra.presentation.dto.response.ErrorResponse;
import br.com.outsera.gra.shared.exception.BusinessException;
import br.com.outsera.gra.shared.exception.DataProcessingException;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.exception.ValidationException;

class GlobalExceptionHandlerTest {

	private final GlobalExceptionHandler handler = new GlobalExceptionHandler();

	@Test
	void handleResourceNotFound_shouldReturn404() {
		ResponseEntity<ErrorResponse> resp = handler.handleResourceNotFoundException(
				new ResourceNotFoundException("Filme", 1L), new org.springframework.mock.web.MockHttpServletRequest());
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
	}

	@Test
	void handleValidation_shouldReturn400() {
		ResponseEntity<ErrorResponse> resp = handler.handleValidationException(new ValidationException("Erro"),
				new org.springframework.mock.web.MockHttpServletRequest());
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
	}

	@Test
	void handleBusiness_shouldReturn422() {
		ResponseEntity<ErrorResponse> resp = handler.handleBusinessException(new BusinessException("Regra"),
				new org.springframework.mock.web.MockHttpServletRequest());
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.UNPROCESSABLE_ENTITY);
	}

	@Test
	void handleDataProcessing_shouldReturn500() {
		ResponseEntity<ErrorResponse> resp = handler.handleDataProcessingException(new DataProcessingException("err"),
				new org.springframework.mock.web.MockHttpServletRequest());
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@Test
	void handleGeneric_shouldReturn500() {
		ResponseEntity<ErrorResponse> resp = handler.handleGenericException(new RuntimeException("boom"),
				new org.springframework.mock.web.MockHttpServletRequest());
		assertThat(resp.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
