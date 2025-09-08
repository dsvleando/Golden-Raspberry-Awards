package br.com.outsera.gra.infrastructure.adapter.inbound;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import br.com.outsera.gra.shared.exception.CsvFileNotFoundException;
import br.com.outsera.gra.shared.exception.CsvProcessingException;
import br.com.outsera.gra.shared.vo.CsvMovieData;

@Component
public class CsvDataLoader {

	@Value("${csv.file.path}")
	private String csvFilePath;

	@Value("${csv.field.separator}")
	private String fieldSeparator;

	@Value("${csv.list.separator}")
	private String listSeparator;

	@Value("${csv.has.header}")
	private Boolean hasHeader;

	private int pointer = 0;
	private Integer totalLines = null;

	public boolean hasNext() {

		if (totalLines == null) {
			totalLines = getTotalLines();
		}

		return pointer < totalLines;
	}

	public int getTotalLines() {
		if (!isCsvFileAvailable()) {
			throw new CsvFileNotFoundException("Arquivo CSV não encontrado: " + csvFilePath);
		}

		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			int lineCount = 0;
			while (reader.readLine() != null) {
				lineCount++;
			}
			return hasHeader ? lineCount - 1 : lineCount;
		} catch (IOException e) {
			throw new CsvProcessingException("Erro ao contar linhas do CSV: " + e.getMessage(), e);
		}
	}

	public List<CsvMovieData> loadMovieDataFromCsv(int batchSize) {
		if (!isCsvFileAvailable()) {
			throw new CsvFileNotFoundException("Arquivo CSV não encontrado: " + csvFilePath);
		}

		List<CsvMovieData> movieDataList = new ArrayList<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(csvFilePath))) {
			String line;
			int currentLine = 0;
			int processedLines = 0;

			if (pointer == 0 && hasHeader && (line = reader.readLine()) != null) {
				currentLine++;
			}

			while (currentLine <= pointer && (line = reader.readLine()) != null) {
				currentLine++;
			}

			while (processedLines < batchSize && (line = reader.readLine()) != null) {
				pointer++;

				if (line.trim().isEmpty()) {
					continue;
				}

				CsvMovieData movieData = parseCsvLine(line);
				if (movieData != null) {
					movieDataList.add(movieData);
					processedLines++;
				}
			}

		} catch (IOException e) {
			throw new CsvProcessingException("Erro ao ler arquivo CSV: " + e.getMessage(), e);
		}

		return movieDataList;
	}

	private boolean isCsvFileAvailable() {
		File csvFile = new File(csvFilePath);
		return csvFile.exists() && csvFile.isFile() && csvFile.canRead();
	}

	private CsvMovieData parseCsvLine(String line) {
		try {
			String[] fields = line.split(fieldSeparator);

			if (fields.length < 4) {
				throw new CsvProcessingException("Linha inválida (campos insuficientes): " + line);
			}

			CsvMovieData movieData = new CsvMovieData();
			Integer yearInt = Integer.parseInt(fields[0].trim());
			String titleStr = fields[1].trim();
			String studiosStr = fields[2].trim();
			String producersStr = fields[3].trim();
			Boolean winnerBl = fields.length > 4 && "yes".equalsIgnoreCase(fields[4].trim()) ? true : false;

			movieData.setYear(yearInt);
			movieData.setTitle(titleStr);
			movieData.setStudios(parseStringList(studiosStr));
			movieData.setProducers(parseStringList(producersStr));
			movieData.setWinner(winnerBl);

			return movieData;

		} catch (NumberFormatException e) {
			throw new CsvProcessingException("Erro ao converter ano para número na linha: " + line, e);
		} catch (Exception e) {
			throw new CsvProcessingException("Erro ao processar linha CSV: " + line, e);
		}
	}

	private List<String> parseStringList(String listStr) {
		if (listStr == null || listStr.trim().isEmpty()) {
			return new ArrayList<>();
		}

		return Arrays.stream(listStr.split(listSeparator)).map(String::trim).filter(s -> !s.isEmpty())
				.collect(java.util.stream.Collectors.toList());
	}
}
