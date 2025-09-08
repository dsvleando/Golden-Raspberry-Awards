package br.com.outsera.gra.infrastructure.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import br.com.outsera.gra.application.service.MovieDataProcessor;
import br.com.outsera.gra.infrastructure.adapter.inbound.CsvDataLoader;
import br.com.outsera.gra.shared.vo.CsvMovieData;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class DataInitializationRunner implements CommandLineRunner {

	private final CsvDataLoader csvDataLoader;
	private final MovieDataProcessor movieDataProcessor;

	@Value("${csv.batch.size}")
	private int batchSize;

	public DataInitializationRunner(CsvDataLoader csvDataLoader, MovieDataProcessor movieDataProcessor) {
		this.csvDataLoader = csvDataLoader;
		this.movieDataProcessor = movieDataProcessor;
	}

	@Override
	public void run(String... args) throws Exception {
		log.info("Iniciando carregamento de dados do CSV...");

		int totalLines = csvDataLoader.getTotalLines();
		log.info("Total de registros no CSV: {}", totalLines);

		int totalProcessed = 0;
		int batchNumber = 1;

		while (csvDataLoader.hasNext()) {
			log.info("Processando lote {}", batchNumber);

			List<CsvMovieData> csvMovieDatas = csvDataLoader.loadMovieDataFromCsv(batchSize);
			int validRecords = csvMovieDatas.size();

			if (validRecords > 0) {
				movieDataProcessor.processCsvData(csvMovieDatas);
				totalProcessed += validRecords;
				log.info("Lote processado! Registros válidos: {} | Total processado: {} / {}", validRecords,
						totalProcessed, totalLines);
			} else {
				log.info("Lote vazio - nenhum registro válido encontrado");
			}
			batchNumber++;
		}

		log.info("Todos os dados foram persistidos com sucesso!");
	}
}
