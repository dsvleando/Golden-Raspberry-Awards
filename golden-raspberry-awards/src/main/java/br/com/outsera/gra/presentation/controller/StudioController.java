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

import br.com.outsera.gra.application.port.inbound.StudioUseCase;
import br.com.outsera.gra.presentation.dto.request.StudioRequest;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.dto.response.StudioResponse;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import br.com.outsera.gra.presentation.mapper.StudioMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/studios")
@RequiredArgsConstructor
@Tag(name = "Studios", description = "Operações relacionadas aos estúdios")
public class StudioController {

	private final StudioUseCase studioUseCase;
	private final StudioMapper studioMapper;
	private final PageMapper pageMapper;

	@PostMapping
	@Operation(summary = "Criar estúdio", description = "Cria um novo estúdio no sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Estúdio criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos") })
	public ResponseEntity<StudioResponse> createStudio(@Valid @RequestBody StudioRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(studioMapper.toResponse(studioUseCase.createStudio(studioMapper.toDomain(request))));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar estúdio por ID", description = "Retorna um estúdio específico pelo seu ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Estúdio encontrado"),
			@ApiResponse(responseCode = "404", description = "Estúdio não encontrado") })
	public ResponseEntity<StudioResponse> getStudioById(
			@Parameter(description = "ID do estúdio") @PathVariable Long id) {
		return studioUseCase.getStudioById(id).map(studio -> ResponseEntity.ok(studioMapper.toResponse(studio)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	@Operation(summary = "Listar estúdios", description = "Retorna uma lista paginada de todos os estúdios")
	@ApiResponse(responseCode = "200", description = "Lista de estúdios retornada com sucesso")
	public ResponseEntity<PageResponse<StudioResponse>> getAllStudios(Pageable pageable) {
		return ResponseEntity
				.ok(pageMapper.toPageResponse(studioUseCase.getAllStudios(pageable), studioMapper::toResponse));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar estúdio", description = "Atualiza um estúdio existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Estúdio atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "404", description = "Estúdio não encontrado") })
	public ResponseEntity<StudioResponse> updateStudio(@Parameter(description = "ID do estúdio") @PathVariable Long id,
			@Valid @RequestBody StudioRequest request) {
		return ResponseEntity
				.ok(studioMapper.toResponse(studioUseCase.updateStudio(id, studioMapper.toDomain(request))));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir estúdio", description = "Remove um estúdio do sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Estúdio excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Estúdio não encontrado") })
	public ResponseEntity<Void> deleteStudio(@Parameter(description = "ID do estúdio") @PathVariable Long id) {
		studioUseCase.deleteStudio(id);
		return ResponseEntity.noContent().build();
	}
}
