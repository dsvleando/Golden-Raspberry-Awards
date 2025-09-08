package br.com.outsera.gra.domain.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Studio {

	private Long id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Studio(String name) {
		this.name = name;
	}
}
