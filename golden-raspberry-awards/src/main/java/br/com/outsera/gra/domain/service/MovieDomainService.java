package br.com.outsera.gra.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.repository.MovieRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.exception.ValidationException;

public class MovieDomainService {

	private final MovieRepository movieRepository;

	public MovieDomainService(MovieRepository movieRepository) {
		this.movieRepository = movieRepository;
	}

	public Movie createMovie(Movie movie) {
		validateMovie(movie);
		return movieRepository.save(movie);
	}

	public Optional<Movie> getMovieById(Long id) {
		return movieRepository.findById(id);
	}

	public Page<Movie> getAllMovies(Pageable pageable) {
		return movieRepository.findAllMovies(pageable);
	}

	public Page<Movie> getWinningMovies(Pageable pageable) {
		return movieRepository.findWinningMovies(pageable);
	}

	public Movie updateMovie(Long id, Movie movie) {
		validateMovie(movie);

		Movie existingMovie = movieRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Filme", id));

		existingMovie.setTitle(movie.getTitle());
		existingMovie.setYear(movie.getYear());
		existingMovie.setWinner(movie.getWinner());

		return movieRepository.updateMovieWithRelationships(existingMovie, movie);
	}

	public void deleteMovie(Long id) {
		if (!movieRepository.findById(id).isPresent()) {
			throw new ResourceNotFoundException("Filme", id);
		}

		movieRepository.deleteById(id);
	}

	public void validateMovie(Movie movie) {
		if (movie == null) {
			throw new ValidationException("Movie não pode ser nulo");
		}
		if (movie.getTitle() == null || movie.getTitle().trim().isEmpty()) {
			throw new ValidationException("Título do filme é obrigatório");
		}
		if (movie.getYear() == null || movie.getYear() <= 0) {
			throw new ValidationException("Ano do filme é obrigatório e deve ser maior que zero");
		}
	}
}
