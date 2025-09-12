package br.com.outsera.gra;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import br.com.outsera.gra.domain.model.Movie;
import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.service.MovieDomainService;
import br.com.outsera.gra.domain.service.ProducerDomainService;
import br.com.outsera.gra.domain.service.StudioDomainService;
import br.com.outsera.gra.infrastructure.persistence.repository.MovieJpaRepository;
import br.com.outsera.gra.infrastructure.persistence.repository.ProducerJpaRepository;
import br.com.outsera.gra.infrastructure.persistence.repository.StudioJpaRepository;

@SpringBootTest
@TestPropertySource(properties = { "spring.datasource.url=jdbc:h2:mem:testdb",
		"spring.jpa.hibernate.ddl-auto=create-drop" })
@Transactional
class MovieUpdateIT {

	@Autowired
	private MovieDomainService movieDomainService;

	@Autowired
	private ProducerDomainService producerDomainService;

	@Autowired
	private StudioDomainService studioDomainService;

	@Autowired
	private MovieJpaRepository movieJpaRepository;

	@Autowired
	private ProducerJpaRepository producerJpaRepository;

	@Autowired
	private StudioJpaRepository studioJpaRepository;

	@Test
	void shouldUpdateMovieWithAllFields() {
		Producer producer1 = producerDomainService.createProducer(new Producer("Test Producer 1"));
		Producer producer2 = producerDomainService.createProducer(new Producer("Test Producer 2"));

		Studio studio1 = studioDomainService.createStudio(new Studio("Test Studio 1"));
		Studio studio2 = studioDomainService.createStudio(new Studio("Test Studio 2"));

		Movie originalMovie = new Movie();
		originalMovie.setTitle("The Room");
		originalMovie.setYear(2003);
		originalMovie.setWinner(Boolean.FALSE);
		originalMovie.setProducers(List.of(producer1));
		originalMovie.setStudios(List.of(studio1));

		Movie savedMovie = movieDomainService.createMovie(originalMovie);
		Long movieId = savedMovie.getId();

		Movie initialMovie = movieDomainService.getMovieById(movieId).orElse(null);
		assertThat(initialMovie.getTitle()).isEqualTo("The Room");
		assertThat(initialMovie.getYear()).isEqualTo(2003);
		assertThat(initialMovie.getWinner()).isFalse();
		assertThat(initialMovie.getProducers()).hasSize(1);
		assertThat(initialMovie.getProducers().get(0).getName()).isEqualTo("Test Producer 1");
		assertThat(initialMovie.getStudios()).hasSize(1);
		assertThat(initialMovie.getStudios().get(0).getName()).isEqualTo("Test Studio 1");

		Movie updateData = new Movie();
		updateData.setTitle("Catwoman");
		updateData.setYear(2004);
		updateData.setWinner(Boolean.TRUE);
		updateData.setProducers(List.of(producer2));
		updateData.setStudios(List.of(studio2));

		Movie updatedMovie = movieDomainService.updateMovie(movieId, updateData);

		assertThat(updatedMovie.getId()).isEqualTo(movieId);
		assertThat(updatedMovie.getTitle()).isEqualTo("Catwoman");
		assertThat(updatedMovie.getYear()).isEqualTo(2004);
		assertThat(updatedMovie.getWinner()).isTrue();
		assertThat(updatedMovie.getProducers()).hasSize(1);
		assertThat(updatedMovie.getProducers().get(0).getName()).isEqualTo("Test Producer 2");
		assertThat(updatedMovie.getStudios()).hasSize(1);
		assertThat(updatedMovie.getStudios().get(0).getName()).isEqualTo("Test Studio 2");

		Movie movieFromDb = movieDomainService.getMovieById(movieId).orElse(null);
		assertThat(movieFromDb.getTitle()).isEqualTo("Catwoman");
		assertThat(movieFromDb.getYear()).isEqualTo(2004);
		assertThat(movieFromDb.getWinner()).isTrue();
		assertThat(movieFromDb.getProducers()).hasSize(1);
		assertThat(movieFromDb.getProducers().get(0).getName()).isEqualTo("Test Producer 2");
		assertThat(movieFromDb.getStudios()).hasSize(1);
		assertThat(movieFromDb.getStudios().get(0).getName()).isEqualTo("Test Studio 2");

		assertThat(movieJpaRepository.count()).isEqualTo(207L);
		assertThat(producerJpaRepository.count()).isEqualTo(362L);
		assertThat(studioJpaRepository.count()).isEqualTo(61L);
	}

	@Test
	void shouldUpdateMovieWithMultipleProducersAndStudios() {
		Producer producer1 = producerDomainService.createProducer(new Producer("Test Producer A"));
		Producer producer2 = producerDomainService.createProducer(new Producer("Test Producer B"));
		Producer producer3 = producerDomainService.createProducer(new Producer("Test Producer C"));

		Studio studio1 = studioDomainService.createStudio(new Studio("Test Studio A"));
		Studio studio2 = studioDomainService.createStudio(new Studio("Test Studio B"));
		Studio studio3 = studioDomainService.createStudio(new Studio("Test Studio C"));

		Movie originalMovie = new Movie();
		originalMovie.setTitle("Battlefield Earth");
		originalMovie.setYear(2000);
		originalMovie.setWinner(Boolean.FALSE);
		originalMovie.setProducers(List.of(producer1));
		originalMovie.setStudios(List.of(studio1));

		Movie savedMovie = movieDomainService.createMovie(originalMovie);
		Long movieId = savedMovie.getId();

		Movie updateData = new Movie();
		updateData.setTitle("The Room");
		updateData.setYear(2003);
		updateData.setWinner(Boolean.TRUE);
		updateData.setProducers(List.of(producer2, producer3));
		updateData.setStudios(List.of(studio2, studio3));

		Movie updatedMovie = movieDomainService.updateMovie(movieId, updateData);

		assertThat(updatedMovie.getId()).isEqualTo(movieId);
		assertThat(updatedMovie.getTitle()).isEqualTo("The Room");
		assertThat(updatedMovie.getYear()).isEqualTo(2003);
		assertThat(updatedMovie.getWinner()).isTrue();
		assertThat(updatedMovie.getProducers()).hasSize(2);
		assertThat(updatedMovie.getProducers()).extracting(Producer::getName)
				.containsExactlyInAnyOrder("Test Producer B", "Test Producer C");
		assertThat(updatedMovie.getStudios()).hasSize(2);
		assertThat(updatedMovie.getStudios()).extracting(Studio::getName).containsExactlyInAnyOrder("Test Studio B",
				"Test Studio C");

		Movie movieFromDb = movieDomainService.getMovieById(movieId).orElse(null);
		assertThat(movieFromDb.getProducers()).hasSize(2);
		assertThat(movieFromDb.getStudios()).hasSize(2);

		assertThat(movieJpaRepository.count()).isEqualTo(207L);
		assertThat(producerJpaRepository.count()).isEqualTo(363L);
		assertThat(studioJpaRepository.count()).isEqualTo(62L);
	}

	@Test
	void shouldUpdateMovieRemovingAllProducersAndStudios() {
		Producer producer1 = producerDomainService.createProducer(new Producer("Test Producer X"));
		Studio studio1 = studioDomainService.createStudio(new Studio("Test Studio X"));

		Movie originalMovie = new Movie();
		originalMovie.setTitle("The Room");
		originalMovie.setYear(2003);
		originalMovie.setWinner(Boolean.FALSE);
		originalMovie.setProducers(List.of(producer1));
		originalMovie.setStudios(List.of(studio1));

		Movie savedMovie = movieDomainService.createMovie(originalMovie);
		Long movieId = savedMovie.getId();

		Movie updateData = new Movie();
		updateData.setTitle("Catwoman");
		updateData.setYear(2004);
		updateData.setWinner(Boolean.TRUE);
		updateData.setProducers(List.of());
		updateData.setStudios(List.of());

		Movie updatedMovie = movieDomainService.updateMovie(movieId, updateData);

		assertThat(updatedMovie.getId()).isEqualTo(movieId);
		assertThat(updatedMovie.getTitle()).isEqualTo("Catwoman");
		assertThat(updatedMovie.getYear()).isEqualTo(2004);
		assertThat(updatedMovie.getWinner()).isTrue();
		assertThat(updatedMovie.getProducers()).isEmpty();
		assertThat(updatedMovie.getStudios()).isEmpty();

		Movie movieFromDb = movieDomainService.getMovieById(movieId).orElse(null);
		assertThat(movieFromDb.getProducers()).isEmpty();
		assertThat(movieFromDb.getStudios()).isEmpty();

		assertThat(producerJpaRepository.count()).isEqualTo(361L);
		assertThat(studioJpaRepository.count()).isEqualTo(60L);
	}
}
