package br.com.outsera.gra.presentation.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Resposta com dados de um estúdio")
public class StudioResponse {

	@Schema(description = "ID único do estúdio", example = "1")
	private Long id;

	@Schema(description = "Nome do estúdio", example = "Wiseau Films")
	private String name;
}
