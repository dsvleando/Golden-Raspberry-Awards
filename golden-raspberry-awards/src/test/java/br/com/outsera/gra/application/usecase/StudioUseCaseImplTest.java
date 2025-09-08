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

import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.service.StudioDomainService;

@ExtendWith(MockitoExtension.class)
class StudioUseCaseImplTest {

	@Mock
	private StudioDomainService service;

	@InjectMocks
	private StudioUseCaseImpl useCase;

	@Test
	void createStudio_delegates() {
		useCase.createStudio(new Studio());
		verify(service).createStudio(any(Studio.class));
	}

	@Test
	void getStudioById_delegates() {
		when(service.getStudioById(1L)).thenReturn(Optional.of(new Studio()));
		useCase.getStudioById(1L);
		verify(service).getStudioById(1L);
	}

	@Test
	void pagination_delegates() {
		when(service.getAllStudios(any(PageRequest.class))).thenReturn(new PageImpl<>(java.util.List.of()));
		useCase.getAllStudios(PageRequest.of(0, 10));
		verify(service).getAllStudios(any(PageRequest.class));
	}
}
