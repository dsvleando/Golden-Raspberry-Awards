package br.com.outsera.gra.presentation.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.presentation.dto.request.MovieRequest;
import br.com.outsera.gra.presentation.dto.response.MovieResponse;
import br.com.outsera.gra.shared.mapper.EntityMapper;

@Component
public class MovieMapper {

	private final EntityMapper entityMapper;

	public MovieMapper(EntityMapper entityMapper) {
		this.entityMapper = entityMapper;
	}

	public Movie toDomain(MovieRequest request) {
		return entityMapper.mapObject(request, Movie.class);
	}

	public MovieResponse toResponse(Movie movie) {
		return entityMapper.mapObject(movie, MovieResponse.class);
	}

	public List<MovieResponse> toResponseList(List<Movie> movies) {
		return entityMapper.mapList(movies, MovieResponse.class);
	}
}
