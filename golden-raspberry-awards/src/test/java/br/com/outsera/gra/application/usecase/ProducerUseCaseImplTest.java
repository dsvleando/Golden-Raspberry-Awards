package br.com.outsera.gra.application.usecase;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.service.ProducerDomainService;

@ExtendWith(MockitoExtension.class)
class ProducerUseCaseImplTest {

	@Mock
	private ProducerDomainService service;

	@InjectMocks
	private ProducerUseCaseImpl useCase;

	@Test
	void createProducer_delegates() {
		useCase.createProducer(new Producer());
		verify(service).createProducer(any(Producer.class));
	}

	@Test
	void getProducerById_delegates() {
		when(service.getProducerById(1L)).thenReturn(Optional.of(new Producer()));
		useCase.getProducerById(1L);
		verify(service).getProducerById(1L);
	}

	@Test
	void pagination_delegates() {
		when(service.getAllProducers(any(PageRequest.class))).thenReturn(new PageImpl<>(java.util.List.of()));
		useCase.getAllProducers(PageRequest.of(0, 10));
		verify(service).getAllProducers(any(PageRequest.class));
	}
}
