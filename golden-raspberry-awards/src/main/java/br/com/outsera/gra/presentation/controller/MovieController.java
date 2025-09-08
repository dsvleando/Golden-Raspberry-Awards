package br.com.outsera.gra.presentation.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.outsera.gra.application.port.inbound.MovieUseCase;
import br.com.outsera.gra.presentation.dto.request.MovieRequest;
import br.com.outsera.gra.presentation.dto.response.MovieResponse;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.mapper.MovieMapper;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/movies")
@RequiredArgsConstructor
@Tag(name = "Movies", description = "Operações relacionadas aos filmes")
public class MovieController {

	private final MovieUseCase movieUseCase;
	private final MovieMapper movieMapper;
	private final PageMapper pageMapper;

	@PostMapping
	@Operation(summary = "Criar filme", description = "Cria um novo filme no sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Filme criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos") })
	public ResponseEntity<MovieResponse> createMovie(@Valid @RequestBody MovieRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(movieMapper.toResponse(movieUseCase.createMovie(movieMapper.toDomain(request))));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar filme por ID", description = "Retorna um filme específico pelo seu ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Filme encontrado"),
			@ApiResponse(responseCode = "404", description = "Filme não encontrado") })
	public ResponseEntity<MovieResponse> getMovieById(@Parameter(description = "ID do filme") @PathVariable Long id) {
		return movieUseCase.getMovieById(id).map(movie -> ResponseEntity.ok(movieMapper.toResponse(movie)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	@Operation(summary = "Listar filmes", description = "Retorna uma lista paginada de todos os filmes")
	@ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso")
	public ResponseEntity<PageResponse<MovieResponse>> getAllMovies(Pageable pageable) {
		PageResponse<MovieResponse> response = pageMapper.toPageResponse(movieUseCase.getAllMovies(pageable),
				movieMapper::toResponse);

		return ResponseEntity.ok(response);
	}

	@GetMapping("/winners")
	@Operation(summary = "Listar filmes vencedores", description = "Retorna uma lista paginada de filmes que ganharam o Golden Raspberry Award")
	@ApiResponse(responseCode = "200", description = "Lista de filmes vencedores retornada com sucesso")
	public ResponseEntity<PageResponse<MovieResponse>> getWinningMovies(Pageable pageable) {
		PageResponse<MovieResponse> response = pageMapper.toPageResponse(movieUseCase.getWinningMovies(pageable),
				movieMapper::toResponse);

		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar filme", description = "Atualiza um filme existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Filme atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "404", description = "Filme não encontrado") })
	public ResponseEntity<MovieResponse> updateMovie(@Parameter(description = "ID do filme") @PathVariable Long id,
			@Valid @RequestBody MovieRequest request) {
		var updatedMovie = movieUseCase.updateMovie(id, movieMapper.toDomain(request));

		return ResponseEntity.ok(movieMapper.toResponse(updatedMovie));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir filme", description = "Remove um filme do sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Filme excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Filme não encontrado") })
	public ResponseEntity<Void> deleteMovie(@Parameter(description = "ID do filme") @PathVariable Long id) {
		movieUseCase.deleteMovie(id);
		return ResponseEntity.noContent().build();
	}
}