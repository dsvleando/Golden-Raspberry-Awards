package br.com.outsera.gra.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Movie;

public interface MovieRepository {

	public Movie save(Movie movie);

	public Optional<Movie> findById(Long id);

	public Page<Movie> findAllMovies(Pageable pageable);

	public Page<Movie> findWinningMovies(Pageable pageable);

	public Movie updateMovieWithRelationships(Movie existingMovie, Movie updatedMovie);

	public void deleteById(Long id);
}
