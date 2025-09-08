package br.com.outsera.gra.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados para criação ou atualização de um produtor")
public class ProducerRequest {

	@NotBlank(message = "Nome do produtor é obrigatório")
	@Schema(description = "Nome do produtor", example = "Tommy Wiseau")
	private String name;
}
