package br.com.outsera.gra.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.repository.ProducerRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class ProducerDomainServiceTest {

	@Mock
	private ProducerRepository producerRepository;

	@InjectMocks
	private ProducerDomainService producerDomainService;

	@Test
	void createProducer_shouldReturnCreatedProducer() {
		Producer inputProducer = new Producer("Harvey Weinstein");
		Producer savedProducer = new Producer("Harvey Weinstein");
		savedProducer.setId(1L);

		when(producerRepository.save(any(Producer.class))).thenReturn(savedProducer);

		Producer result = producerDomainService.createProducer(inputProducer);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getName()).isEqualTo("Harvey Weinstein");
		verify(producerRepository).save(inputProducer);
	}

	@Test
	void findOrCreateProducerByName_shouldReturnExistingProducer_whenFound() {
		String producerName = "Joel Silver";
		Producer existingProducer = new Producer(producerName);
		existingProducer.setId(2L);

		when(producerRepository.findByNameEqualsIgnoreCase(producerName)).thenReturn(Optional.of(existingProducer));

		Producer result = producerDomainService.findOrCreateProducerByName(producerName);

		assertThat(result).isEqualTo(existingProducer);
		assertThat(result.getId()).isEqualTo(2L);
		verify(producerRepository).findByNameEqualsIgnoreCase(producerName);
		verify(producerRepository, never()).save(any(Producer.class));
	}

	@Test
	void findOrCreateProducerByName_shouldCreateNewProducer_whenNotFound() {
		String producerName = "Steven Spielberg";
		Producer newProducer = new Producer(producerName);
		newProducer.setId(3L);

		when(producerRepository.findByNameEqualsIgnoreCase(producerName)).thenReturn(Optional.empty());
		when(producerRepository.save(any(Producer.class))).thenReturn(newProducer);

		Producer result = producerDomainService.findOrCreateProducerByName(producerName);

		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo(producerName);
		assertThat(result.getId()).isEqualTo(3L);
		verify(producerRepository).findByNameEqualsIgnoreCase(producerName);
		verify(producerRepository).save(any(Producer.class));
	}

	@Test
	void getProducerById_shouldReturnProducer_whenFound() {
		Long producerId = 1L;
		Producer producer = new Producer("Harvey Weinstein");
		producer.setId(producerId);

		when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));

		Producer result = producerDomainService.getProducerById(producerId).orElse(null);

		assertThat(result).isEqualTo(producer);
		assertThat(result.getId()).isEqualTo(producerId);
		assertThat(result.getName()).isEqualTo("Harvey Weinstein");
		verify(producerRepository).findById(producerId);
	}

	@Test
	void getProducerById_shouldReturnEmpty_whenNotFound() {
		Long producerId = 999L;
		when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

		Optional<Producer> result = producerDomainService.getProducerById(producerId);
		
		assertThat(result).isEmpty();
		verify(producerRepository).findById(producerId);
	}

	@Test
	void getAllProducers_shouldReturnPaginatedProducers() {
		Producer producer1 = new Producer("Harvey Weinstein");
		producer1.setId(1L);
		Producer producer2 = new Producer("Joel Silver");
		producer2.setId(2L);

		Pageable pageable = PageRequest.of(0, 10);
		PageImpl<Producer> expectedPage = new PageImpl<>(List.of(producer1, producer2), pageable, 2);

		when(producerRepository.findAllProducers(pageable)).thenReturn(expectedPage);

		Page<Producer> result = producerDomainService.getAllProducers(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).containsExactly(producer1, producer2);
		assertThat(result.getTotalElements()).isEqualTo(2);
		verify(producerRepository).findAllProducers(pageable);
	}

	@Test
	void updateProducer_shouldUpdateAndReturnProducer_whenFound() {
		Long producerId = 1L;
		Producer existingProducer = new Producer("Old Name");
		existingProducer.setId(producerId);

		Producer updateData = new Producer("New Harvey Weinstein");
		Producer updatedProducer = new Producer("New Harvey Weinstein");
		updatedProducer.setId(producerId);

		when(producerRepository.findById(producerId)).thenReturn(Optional.of(existingProducer));
		when(producerRepository.save(any(Producer.class))).thenReturn(updatedProducer);

		Producer result = producerDomainService.updateProducer(producerId, updateData);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(producerId);
		assertThat(result.getName()).isEqualTo("New Harvey Weinstein");
		verify(producerRepository).findById(producerId);
		verify(producerRepository).save(any(Producer.class));
	}

	@Test
	void updateProducer_shouldThrow_whenNotFound() {
		Long producerId = 999L;
		Producer updateData = new Producer("Harvey Weinstein");

		when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> producerDomainService.updateProducer(producerId, updateData))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Produtor não encontrado com ID: 999");
		verify(producerRepository).findById(producerId);
		verify(producerRepository, never()).save(any(Producer.class));
	}

	@Test
	void deleteProducer_shouldCallRepositoryDelete_whenFound() {
		Long producerId = 1L;
		Producer producer = new Producer("Harvey Weinstein");
		producer.setId(producerId);

		when(producerRepository.findById(producerId)).thenReturn(Optional.of(producer));

		producerDomainService.deleteProducer(producerId);

		verify(producerRepository).findById(producerId);
		verify(producerRepository).deleteById(producerId);
	}

	@Test
	void deleteProducer_shouldThrow_whenNotFound() {
		Long producerId = 999L;
		when(producerRepository.findById(producerId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> producerDomainService.deleteProducer(producerId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Produtor não encontrado com ID: 999");
		verify(producerRepository).findById(producerId);
		verify(producerRepository, never()).deleteById(anyLong());
	}
}
