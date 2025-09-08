package br.com.outsera.gra.infrastructure.adapter.outbound;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.repository.MovieRepository;
import br.com.outsera.gra.infrastructure.persistence.entity.MovieEntity;
import br.com.outsera.gra.infrastructure.persistence.repository.MovieJpaRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaMovieRepositoryAdapter implements MovieRepository {

	private final MovieJpaRepository movieJpaRepository;
	private final EntityMapper entityMapper;

	@Override
	public Movie save(Movie movie) {
		return entityMapper.mapObject(movieJpaRepository.save(entityMapper.mapObject(movie, MovieEntity.class)),
				Movie.class);
	}

	@Override
	@Transactional
	public Movie updateMovieWithRelationships(Movie existingMovie, Movie updatedMovie) {

		MovieEntity existingEntity = movieJpaRepository.findById(existingMovie.getId())
				.orElseThrow(() -> new ResourceNotFoundException("Filme", existingMovie.getId()));

		MovieEntity updatedEntity = entityMapper.mapObject(updatedMovie, MovieEntity.class);

		existingEntity.setTitle(updatedEntity.getTitle());
		existingEntity.setYear(updatedEntity.getYear());
		existingEntity.setWinner(updatedEntity.getWinner());

		if (updatedEntity.getStudios() != null) {
			existingEntity.setStudios(updatedEntity.getStudios());
		}

		if (updatedEntity.getProducers() != null) {
			existingEntity.setProducers(updatedEntity.getProducers());
		}

		return entityMapper.mapObject(movieJpaRepository.save(existingEntity), Movie.class);
	}

	@Override
	public Optional<Movie> findById(Long id) {
		return movieJpaRepository.findById(id).map(entity -> entityMapper.mapObject(entity, Movie.class));
	}

	@Override
	public Page<Movie> findAllMovies(Pageable pageable) {
		return movieJpaRepository.findAll(pageable).map(entity -> entityMapper.mapObject(entity, Movie.class));
	}

	@Override
	public Page<Movie> findWinningMovies(Pageable pageable) {
		return movieJpaRepository.findByWinner(Boolean.TRUE, pageable)
				.map(entity -> entityMapper.mapObject(entity, Movie.class));
	}

	@Override
	public void deleteById(Long id) {
		movieJpaRepository.deleteById(id);
	}
}
