package br.com.outsera.gra.application.usecase;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.application.port.inbound.MovieUseCase;
import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.service.MovieDomainService;

public class MovieUseCaseImpl implements MovieUseCase {

	private final MovieDomainService movieDomainService;

	public MovieUseCaseImpl(MovieDomainService movieDomainService) {
		this.movieDomainService = movieDomainService;
	}

	@Override
	public Movie createMovie(Movie movie) {
		return movieDomainService.createMovie(movie);
	}

	@Override
	public Optional<Movie> getMovieById(Long id) {
		return movieDomainService.getMovieById(id);
	}

	@Override
	public Page<Movie> getAllMovies(Pageable pageable) {
		return movieDomainService.getAllMovies(pageable);
	}

	@Override
	public Page<Movie> getWinningMovies(Pageable pageable) {
		return movieDomainService.getWinningMovies(pageable);
	}

	@Override
	public Movie updateMovie(Long id, Movie movie) {
		return movieDomainService.updateMovie(id, movie);
	}

	@Override
	public void deleteMovie(Long id) {
		movieDomainService.deleteMovie(id);
	}
}
