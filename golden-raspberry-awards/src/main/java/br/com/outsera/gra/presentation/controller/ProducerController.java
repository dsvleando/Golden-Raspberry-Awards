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

import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.presentation.dto.request.ProducerRequest;
import br.com.outsera.gra.presentation.dto.response.PageResponse;
import br.com.outsera.gra.presentation.dto.response.ProducerAwardIntervalsResponse;
import br.com.outsera.gra.presentation.dto.response.ProducerResponse;
import br.com.outsera.gra.presentation.mapper.PageMapper;
import br.com.outsera.gra.presentation.mapper.ProducerMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/producers")
@RequiredArgsConstructor
@Tag(name = "Producers", description = "Operações relacionadas aos produtores")
public class ProducerController {

	private final ProducerUseCase producerUseCase;
	private final ProducerMapper producerMapper;
	private final PageMapper pageMapper;

	@PostMapping
	@Operation(summary = "Criar produtor", description = "Cria um novo produtor no sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Produtor criado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos") })
	public ResponseEntity<ProducerResponse> createProducer(@Valid @RequestBody ProducerRequest request) {
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(producerMapper.toResponse(producerUseCase.createProducer(producerMapper.toDomain(request))));
	}

	@GetMapping("/{id}")
	@Operation(summary = "Buscar produtor por ID", description = "Retorna um produtor específico pelo seu ID")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Produtor encontrado"),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado") })
	public ResponseEntity<ProducerResponse> getProducerById(
			@Parameter(description = "ID do produtor") @PathVariable Long id) {
		return producerUseCase.getProducerById(id)
				.map(producer -> ResponseEntity.ok(producerMapper.toResponse(producer)))
				.orElse(ResponseEntity.notFound().build());
	}

	@GetMapping
	@Operation(summary = "Listar produtores", description = "Retorna uma lista paginada de todos os produtores")
	@ApiResponse(responseCode = "200", description = "Lista de produtores retornada com sucesso")
	public ResponseEntity<PageResponse<ProducerResponse>> getAllProducers(Pageable pageable) {
		return ResponseEntity
				.ok(pageMapper.toPageResponse(producerUseCase.getAllProducers(pageable), producerMapper::toResponse));
	}

	@PutMapping("/{id}")
	@Operation(summary = "Atualizar produtor", description = "Atualiza um produtor existente")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Produtor atualizado com sucesso"),
			@ApiResponse(responseCode = "400", description = "Dados inválidos"),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado") })
	public ResponseEntity<ProducerResponse> updateProducer(
			@Parameter(description = "ID do produtor") @PathVariable Long id,
			@Valid @RequestBody ProducerRequest request) {
		return ResponseEntity
				.ok(producerMapper.toResponse(producerUseCase.updateProducer(id, producerMapper.toDomain(request))));
	}

	@DeleteMapping("/{id}")
	@Operation(summary = "Excluir produtor", description = "Remove um produtor do sistema")
	@ApiResponses(value = { @ApiResponse(responseCode = "204", description = "Produtor excluído com sucesso"),
			@ApiResponse(responseCode = "404", description = "Produtor não encontrado") })
	public ResponseEntity<Void> deleteProducer(@Parameter(description = "ID do produtor") @PathVariable Long id) {
		producerUseCase.deleteProducer(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/award-intervals")
	@Operation(summary = "Intervalos de prêmios", description = "Retorna os produtores com maior e menor intervalo entre vitórias consecutivas")
	@ApiResponse(responseCode = "200", description = "Intervalos de prêmios retornados com sucesso")
	public ResponseEntity<ProducerAwardIntervalsResponse> getProducerAwardIntervals() {
		return ResponseEntity.ok(producerMapper.toResponse(producerUseCase.getProducerAwardIntervals()));
	}

}
