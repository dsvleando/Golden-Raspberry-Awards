package br.com.outsera.gra.presentation.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.outsera.gra.application.port.inbound.MovieUseCase;
import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.presentation.dto.request.MovieRequest;
import br.com.outsera.gra.presentation.dto.request.ProducerLinkRequest;
import br.com.outsera.gra.presentation.dto.request.StudioLinkRequest;
import br.com.outsera.gra.presentation.dto.response.MovieResponse;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.mapper.MovieMapper;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import br.com.outsera.gra.shared.exception.BusinessException;
import br.com.outsera.gra.shared.exception.DataProcessingException;

@WebMvcTest(controllers = MovieController.class)
class MovieControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private MovieUseCase movieUseCase;

	@MockBean
	private MovieMapper movieMapper;

	@MockBean
	private PageMapper pageMapper;

	@Test
	void createMovie_shouldReturn201() throws Exception {
		MovieRequest req = new MovieRequest();
		req.setTitle("Catwoman");
		req.setYear(2003);
		req.setWinner(Boolean.FALSE);
		StudioLinkRequest s = new StudioLinkRequest();
		s.setId(1L);
		ProducerLinkRequest p = new ProducerLinkRequest();
		p.setId(1L);
		req.setStudios(List.of(s));
		req.setProducers(List.of(p));

		Movie domain = new Movie();
		MovieResponse resp = new MovieResponse();

		given(movieMapper.toDomain(any(MovieRequest.class))).willReturn(domain);
		given(movieUseCase.createMovie(any(Movie.class))).willReturn(domain);
		given(movieMapper.toResponse(any(Movie.class))).willReturn(resp);

		mockMvc.perform(post("/api/movies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isCreated());

		verify(movieUseCase).createMovie(any(Movie.class));
	}

	@Test
	void getMovieById_shouldReturn200_whenFound() throws Exception {
		Movie domain = new Movie();
		MovieResponse resp = new MovieResponse();
		given(movieUseCase.getMovieById(1L)).willReturn(Optional.of(domain));
		given(movieMapper.toResponse(domain)).willReturn(resp);

		mockMvc.perform(get("/api/movies/{id}", 1L)).andExpect(status().isOk());
	}

	@Test
	void updateMovie_shouldReturn200() throws Exception {
		MovieRequest req = new MovieRequest();
		req.setTitle("Gigli");
		req.setYear(2004);
		req.setWinner(Boolean.TRUE);
		Movie domain = new Movie();
		MovieResponse resp = new MovieResponse();

		given(movieMapper.toDomain(any(MovieRequest.class))).willReturn(domain);
		given(movieUseCase.updateMovie(eq(1L), any(Movie.class))).willReturn(domain);
		given(movieMapper.toResponse(domain)).willReturn(resp);

		mockMvc.perform(put("/api/movies/{id}", 1L).contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isOk());
	}

	@Test
	void deleteMovie_shouldReturn204() throws Exception {
		mockMvc.perform(delete("/api/movies/{id}", 1L)).andExpect(status().isNoContent());
		verify(movieUseCase).deleteMovie(1L);
	}

	@Test
	void getAllMovies_shouldReturn200() throws Exception {
		Page<Movie> page = new PageImpl<>(java.util.List.of(), PageRequest.of(0, 10), 0);
		given(movieUseCase.getAllMovies(any())).willReturn(page);
		PageResponse<MovieResponse> pr = PageResponse.of(new PageImpl<MovieResponse>(java.util.List.of()));
		given(pageMapper.toPageResponse(Mockito.<Page<Movie>>any(), Mockito.<Function<Movie, MovieResponse>>any()))
				.willReturn(pr);

		mockMvc.perform(get("/api/movies")).andExpect(status().isOk());
	}

	@Test
	void getWinningMovies_shouldReturn200() throws Exception {
		Page<Movie> page = new PageImpl<>(java.util.List.of(), PageRequest.of(0, 10), 0);
		given(movieUseCase.getWinningMovies(any())).willReturn(page);
		PageResponse<MovieResponse> pr2 = PageResponse.of(new PageImpl<MovieResponse>(java.util.List.of()));
		given(pageMapper.toPageResponse(Mockito.<Page<Movie>>any(), Mockito.<Function<Movie, MovieResponse>>any()))
				.willReturn(pr2);

		mockMvc.perform(get("/api/movies/winners")).andExpect(status().isOk());
	}

	@Test
	void getMovieById_shouldReturn404_whenNotFound() throws Exception {
		given(movieUseCase.getMovieById(99L)).willReturn(java.util.Optional.empty());
		mockMvc.perform(get("/api/movies/{id}", 99L)).andExpect(status().isNotFound());
	}

	@Test
	void createMovie_shouldReturn400_whenInvalidBody() throws Exception {
		MovieRequest req = new MovieRequest();
		mockMvc.perform(post("/api/movies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isBadRequest());
	}

	@Test
	void createMovie_shouldReturn422_whenBusinessException() throws Exception {
		MovieRequest req = new MovieRequest();
		req.setTitle("Catwoman");
		req.setYear(2004);
		given(movieMapper.toDomain(any(MovieRequest.class))).willReturn(new Movie());
		given(movieUseCase.createMovie(any(Movie.class))).willThrow(new BusinessException("Regra violada"));

		mockMvc.perform(post("/api/movies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isUnprocessableEntity());
	}

	@Test
	void createMovie_shouldReturn500_whenDataProcessingException() throws Exception {
		MovieRequest req = new MovieRequest();
		req.setTitle("Gigli");
		req.setYear(2003);
		given(movieMapper.toDomain(any(MovieRequest.class))).willReturn(new Movie());
		given(movieUseCase.createMovie(any(Movie.class))).willThrow(new DataProcessingException("Erro"));

		mockMvc.perform(post("/api/movies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isInternalServerError());
	}

	@Test
	void createMovie_shouldReturn500_whenUnexpectedException() throws Exception {
		MovieRequest req = new MovieRequest();
		req.setTitle("Gigli");
		req.setYear(2003);
		given(movieMapper.toDomain(any(MovieRequest.class))).willReturn(new Movie());
		given(movieUseCase.createMovie(any(Movie.class))).willThrow(new RuntimeException("boom"));

		mockMvc.perform(post("/api/movies").contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(req))).andExpect(status().isInternalServerError());
	}
}
