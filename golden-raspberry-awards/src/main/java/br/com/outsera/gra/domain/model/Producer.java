package br.com.outsera.gra.domain.model;

import java.time.LocalDateTime;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Producer {

	private Long id;
	private String name;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public Producer(String name) {
		this.name = name;
	}

}
