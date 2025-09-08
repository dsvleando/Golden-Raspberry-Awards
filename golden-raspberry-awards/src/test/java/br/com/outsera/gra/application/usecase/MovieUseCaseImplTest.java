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

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.service.MovieDomainService;

@ExtendWith(MockitoExtension.class)
class MovieUseCaseImplTest {

	@Mock
	private MovieDomainService service;

	@InjectMocks
	private MovieUseCaseImpl useCase;

	@Test
	void createMovie_delegates() {
		useCase.createMovie(new Movie());
		verify(service).createMovie(any(Movie.class));
	}

	@Test
	void getMovieById_delegates() {
		when(service.getMovieById(1L)).thenReturn(Optional.of(new Movie()));
		useCase.getMovieById(1L);
		verify(service).getMovieById(1L);
	}

	@Test
	void pagination_delegates() {
		when(service.getAllMovies(any(PageRequest.class))).thenReturn(new PageImpl<>(java.util.List.of()));
		useCase.getAllMovies(PageRequest.of(0, 10));
		verify(service).getAllMovies(any(PageRequest.class));
	}
}
