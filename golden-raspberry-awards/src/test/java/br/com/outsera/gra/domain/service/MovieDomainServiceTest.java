package br.com.outsera.gra.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.repository.MovieRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.exception.ValidationException;

@ExtendWith(MockitoExtension.class)
class MovieDomainServiceTest {

	@Mock
	private MovieRepository movieRepository;

	@InjectMocks
	private MovieDomainService movieDomainService;

	private Movie validMovie;

	@BeforeEach
	void setUp() {
		validMovie = new Movie();
		validMovie.setTitle("The Room");
		validMovie.setYear(2003);
		validMovie.setWinner(Boolean.FALSE);
	}

	@Test
	void validateMovie_shouldThrow_whenNull() {
		assertThatThrownBy(() -> movieDomainService.validateMovie(null)).isInstanceOf(ValidationException.class)
				.hasMessageContaining("Movie não pode ser nulo");
	}

	@Test
	void validateMovie_shouldThrow_whenEmptyTitle() {
		Movie m = new Movie();
		m.setTitle(" ");
		m.setYear(2020);
		assertThatThrownBy(() -> movieDomainService.validateMovie(m)).isInstanceOf(ValidationException.class)
				.hasMessageContaining("Título do filme é obrigatório");
	}

	@Test
	void validateMovie_shouldThrow_whenNullTitle() {
		Movie m = new Movie();
		m.setTitle(null);
		m.setYear(2020);
		assertThatThrownBy(() -> movieDomainService.validateMovie(m)).isInstanceOf(ValidationException.class)
				.hasMessageContaining("Título do filme é obrigatório");
	}

	@Test
	void validateMovie_shouldThrow_whenInvalidYear() {
		Movie m = new Movie();
		m.setTitle("Catwoman");
		m.setYear(0);
		assertThatThrownBy(() -> movieDomainService.validateMovie(m)).isInstanceOf(ValidationException.class)
				.hasMessageContaining("Ano do filme é obrigatório e deve ser maior que zero");
	}

	@Test
	void validateMovie_shouldThrow_whenNegativeYear() {
		Movie m = new Movie();
		m.setTitle("Catwoman");
		m.setYear(-1);
		assertThatThrownBy(() -> movieDomainService.validateMovie(m)).isInstanceOf(ValidationException.class)
				.hasMessageContaining("Ano do filme é obrigatório e deve ser maior que zero");
	}

	@Test
	void validateMovie_shouldPass_whenValid() {
		movieDomainService.validateMovie(validMovie);
	}

	@Test
	void createMovie_shouldReturnCreatedMovie() {
		Movie savedMovie = new Movie();
		savedMovie.setId(1L);
		savedMovie.setTitle("The Room");
		savedMovie.setYear(2003);
		savedMovie.setWinner(Boolean.FALSE);

		when(movieRepository.save(any(Movie.class))).thenReturn(savedMovie);

		Movie result = movieDomainService.createMovie(validMovie);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getTitle()).isEqualTo("The Room");
		assertThat(result.getYear()).isEqualTo(2003);
		assertThat(result.getWinner()).isFalse();
		verify(movieRepository).save(validMovie);
	}

	@Test
	void getMovieById_shouldReturnMovie_whenFound() {
		Long movieId = 1L;
		Movie movie = new Movie();
		movie.setId(movieId);
		movie.setTitle("The Room");
		movie.setYear(2003);
		movie.setWinner(Boolean.FALSE);

		when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

		Movie result = movieDomainService.getMovieById(movieId).orElse(null);

		assertThat(result).isEqualTo(movie);
		assertThat(result.getId()).isEqualTo(movieId);
		assertThat(result.getTitle()).isEqualTo("The Room");
		verify(movieRepository).findById(movieId);
	}

	@Test
	void getMovieById_shouldReturnEmpty_whenNotFound() {
		Long movieId = 999L;
		when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

		Optional<Movie> result = movieDomainService.getMovieById(movieId);
		
		assertThat(result).isEmpty();
		verify(movieRepository).findById(movieId);
	}

	@Test
	void getAllMovies_shouldReturnPaginatedMovies() {
		Movie movie1 = new Movie();
		movie1.setId(1L);
		movie1.setTitle("The Room");
		movie1.setYear(2003);
		movie1.setWinner(Boolean.FALSE);

		Movie movie2 = new Movie();
		movie2.setId(2L);
		movie2.setTitle("Catwoman");
		movie2.setYear(2004);
		movie2.setWinner(Boolean.TRUE);

		Pageable pageable = PageRequest.of(0, 10);
		PageImpl<Movie> expectedPage = new PageImpl<>(List.of(movie1, movie2), pageable, 2);

		when(movieRepository.findAllMovies(pageable)).thenReturn(expectedPage);

		Page<Movie> result = movieDomainService.getAllMovies(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).containsExactly(movie1, movie2);
		assertThat(result.getTotalElements()).isEqualTo(2);
		verify(movieRepository).findAllMovies(pageable);
	}

	@Test
	void getWinningMovies_shouldReturnPaginatedWinningMovies() {
		Movie winningMovie1 = new Movie();
		winningMovie1.setId(1L);
		winningMovie1.setTitle("Catwoman");
		winningMovie1.setYear(2004);
		winningMovie1.setWinner(Boolean.TRUE);

		Movie winningMovie2 = new Movie();
		winningMovie2.setId(2L);
		winningMovie2.setTitle("Battlefield Earth");
		winningMovie2.setYear(2000);
		winningMovie2.setWinner(Boolean.TRUE);

		Pageable pageable = PageRequest.of(0, 10);
		PageImpl<Movie> expectedPage = new PageImpl<>(List.of(winningMovie1, winningMovie2), pageable, 2);

		when(movieRepository.findWinningMovies(pageable)).thenReturn(expectedPage);

		Page<Movie> result = movieDomainService.getWinningMovies(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).containsExactly(winningMovie1, winningMovie2);
		assertThat(result.getTotalElements()).isEqualTo(2);
		verify(movieRepository).findWinningMovies(pageable);
	}

	@Test
	void updateMovie_shouldUpdateAndReturnMovie_whenFound() {
		Long movieId = 1L;
		Movie existingMovie = new Movie();
		existingMovie.setId(movieId);
		existingMovie.setTitle("Old Title");
		existingMovie.setYear(2000);
		existingMovie.setWinner(Boolean.FALSE);

		Movie updateData = new Movie();
		updateData.setTitle("The Room");
		updateData.setYear(2003);
		updateData.setWinner(Boolean.FALSE);

		Movie updatedMovie = new Movie();
		updatedMovie.setId(movieId);
		updatedMovie.setTitle("The Room");
		updatedMovie.setYear(2003);
		updatedMovie.setWinner(Boolean.FALSE);

		when(movieRepository.findById(movieId)).thenReturn(Optional.of(existingMovie));
		when(movieRepository.updateMovieWithRelationships(any(Movie.class), any(Movie.class))).thenReturn(updatedMovie);

		Movie result = movieDomainService.updateMovie(movieId, updateData);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(movieId);
		assertThat(result.getTitle()).isEqualTo("The Room");
		assertThat(result.getYear()).isEqualTo(2003);
		assertThat(result.getWinner()).isFalse();
		verify(movieRepository).findById(movieId);
		verify(movieRepository).updateMovieWithRelationships(any(Movie.class), any(Movie.class));
	}

	@Test
	void updateMovie_shouldThrow_whenNotFound() {
		Long movieId = 999L;
		when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movieDomainService.updateMovie(movieId, validMovie))
				.isInstanceOf(ResourceNotFoundException.class).hasMessageContaining("Filme não encontrado com ID: 999");
		verify(movieRepository).findById(movieId);
		verify(movieRepository, never()).updateMovieWithRelationships(any(Movie.class), any(Movie.class));
	}

	@Test
	void deleteMovie_shouldCallRepositoryDelete_whenFound() {
		Long movieId = 1L;
		Movie movie = new Movie();
		movie.setId(movieId);
		movie.setTitle("The Room");
		movie.setYear(2003);
		movie.setWinner(Boolean.FALSE);

		when(movieRepository.findById(movieId)).thenReturn(Optional.of(movie));

		movieDomainService.deleteMovie(movieId);

		verify(movieRepository).findById(movieId);
		verify(movieRepository).deleteById(movieId);
	}

	@Test
	void deleteMovie_shouldThrow_whenNotFound() {
		Long movieId = 999L;
		when(movieRepository.findById(movieId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> movieDomainService.deleteMovie(movieId)).isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Filme não encontrado com ID: 999");
		verify(movieRepository).findById(movieId);
		verify(movieRepository, never()).deleteById(anyLong());
	}
}
