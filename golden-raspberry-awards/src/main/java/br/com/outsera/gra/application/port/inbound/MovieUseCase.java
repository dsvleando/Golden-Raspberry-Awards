package br.com.outsera.gra.application.port.inbound;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Movie;

public interface MovieUseCase {

	public Movie createMovie(Movie movie);

	public Optional<Movie> getMovieById(Long id);

	public Page<Movie> getAllMovies(Pageable pageable);

	public Page<Movie> getWinningMovies(Pageable pageable);

	public Movie updateMovie(Long id, Movie movie);

	public void deleteMovie(Long id);
}
