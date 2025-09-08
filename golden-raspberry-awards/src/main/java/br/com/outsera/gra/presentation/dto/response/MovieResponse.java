package br.com.outsera.gra.presentation.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com dados de um filme")
public class MovieResponse {

	@Schema(description = "ID único do filme", example = "1")
	private Long id;

	@Schema(description = "Título do filme", example = "The Room")
	private String title;

	@Schema(description = "Lista de estúdios que produziram o filme")
	private List<StudioResponse> studios;

	@Schema(description = "Lista de produtores do filme")
	private List<ProducerResponse> producers;

	@Schema(description = "Indica se o filme ganhou o Golden Raspberry Award", example = "true")
	private Boolean winner;

	@Schema(description = "Ano de lançamento do filme", example = "2003")
	private Integer year;

	@Schema(description = "Data e hora de criação do registro")
	private LocalDateTime createdAt;

	@Schema(description = "Data e hora da última atualização do registro")
	private LocalDateTime updatedAt;
}
