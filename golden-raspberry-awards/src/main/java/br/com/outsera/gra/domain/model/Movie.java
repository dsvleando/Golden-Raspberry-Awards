package br.com.outsera.gra.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class Movie {

	private Long id;
	private String title;
	private List<Studio> studios;
	private List<Producer> producers;
	private Boolean winner;
	private Integer year;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
}
