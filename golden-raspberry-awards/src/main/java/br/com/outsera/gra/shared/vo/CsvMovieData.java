package br.com.outsera.gra.shared.vo;

import java.util.List;

import lombok.Data;

@Data
public class CsvMovieData {

	private Integer year;
	private String title;
	private List<String> studios;
	private List<String> producers;
	private Boolean winner;
}
