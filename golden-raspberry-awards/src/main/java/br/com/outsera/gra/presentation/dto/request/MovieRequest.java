package br.com.outsera.gra.presentation.dto.request;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
@Schema(description = "Dados para criação ou atualização de um filme")
public class MovieRequest {

	@NotBlank(message = "Título é obrigatório")
	@Schema(description = "Título do filme", example = "The Room")
	private String title;

	@NotNull(message = "Ano é obrigatório")
	@Positive(message = "Ano deve ser positivo")
	@Schema(description = "Ano de lançamento do filme", example = "2003")
	private Integer year;

	@Schema(description = "Indica se o filme ganhou o Golden Raspberry Award", example = "true")
	private Boolean winner;

	@Schema(description = "Lista de estúdios que produziram o filme (referenciados por id)", example = "[{\"id\": 1}]")
	private List<StudioLinkRequest> studios;

	@Schema(description = "Lista de produtores do filme (referenciados por id)", example = "[{\"id\": 1}]")
	private List<ProducerLinkRequest> producers;
}
