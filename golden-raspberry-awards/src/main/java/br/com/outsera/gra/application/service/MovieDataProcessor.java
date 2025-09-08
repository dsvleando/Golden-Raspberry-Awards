package br.com.outsera.gra.application.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.outsera.gra.application.port.inbound.MovieUseCase;
import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.application.port.inbound.StudioUseCase;
import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.shared.vo.CsvMovieData;

@Service
public class MovieDataProcessor {

	private final MovieUseCase movieUseCase;
	private final StudioUseCase studioUseCase;
	private final ProducerUseCase producerUseCase;

	public MovieDataProcessor(MovieUseCase movieUseCase, StudioUseCase studioUseCase, ProducerUseCase producerUseCase) {
		this.movieUseCase = movieUseCase;
		this.studioUseCase = studioUseCase;
		this.producerUseCase = producerUseCase;
	}

	@Transactional
	public void processCsvData(List<CsvMovieData> csvData) {

		for (CsvMovieData csvMovie : csvData) {
			movieUseCase.createMovie(mapDataToModel(csvMovie));
		}
	}

	private Movie mapDataToModel(CsvMovieData csvMovieData) {

		Movie movie = new Movie();
		movie.setYear(csvMovieData.getYear());
		movie.setTitle(csvMovieData.getTitle());
		movie.setWinner(csvMovieData.getWinner());
		movie.setProducers(processProducers(csvMovieData.getProducers()));
		movie.setStudios(processStudios(csvMovieData.getStudios()));

		return movie;
	}

	private List<Studio> processStudios(List<String> studioNames) {
		return studioNames.stream().map(studioUseCase::findOrCreateStudioByName).collect(Collectors.toList());
	}

	private List<Producer> processProducers(List<String> producerNames) {
		return producerNames.stream().map(producerUseCase::findOrCreateProducerByName).collect(Collectors.toList());
	}

}
