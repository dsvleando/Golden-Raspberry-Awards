package br.com.outsera.gra;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.outsera.gra.infrastructure.persistence.repository.MovieJpaRepository;
import br.com.outsera.gra.infrastructure.persistence.repository.ProducerJpaRepository;
import br.com.outsera.gra.infrastructure.persistence.repository.StudioJpaRepository;

@SpringBootTest
class DataLoadIT {

	@Autowired
	private MovieJpaRepository movieJpaRepository;

	@Autowired
	private ProducerJpaRepository producerJpaRepository;

	@Autowired
	private StudioJpaRepository studioJpaRepository;

	@Test
	void shouldLoadCsvOnStartup() {
		assertThat(movieJpaRepository.count()).isEqualTo(206L);
		assertThat(producerJpaRepository.count()).isEqualTo(300L);
		assertThat(studioJpaRepository.count()).isEqualTo(59L);
	}
}
