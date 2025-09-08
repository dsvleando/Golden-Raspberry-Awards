package br.com.outsera.gra.presentation.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
@Schema(description = "Dados para criação ou atualização de um estúdio")
public class StudioRequest {

	@NotBlank(message = "Nome do estúdio é obrigatório")
	@Schema(description = "Nome do estúdio", example = "Wiseau Films")
	private String name;
}
