package br.com.outsera.gra.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.repository.ProducerRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

public class ProducerDomainService {

	private final ProducerRepository producerRepository;

	public ProducerDomainService(ProducerRepository producerRepository) {
		this.producerRepository = producerRepository;
	}

	public Producer findOrCreateProducerByName(String name) {
		validateProducerName(name);

		Optional<Producer> existingProducer = producerRepository.findByNameEqualsIgnoreCase(name);
		if (existingProducer.isPresent()) {
			return existingProducer.get();
		}

		return createProducer(name);
	}

	public Producer createProducer(Producer producer) {
		return producerRepository.save(producer);
	}

	private Producer createProducer(String name) {
		return createProducer(new Producer(name));

	}

	private void validateProducerName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do produtor é obrigatório");
		}
	}

	public Optional<Producer> getProducerById(Long id) {
		return producerRepository.findById(id);
	}

	public Page<Producer> getAllProducers(Pageable pageable) {
		return producerRepository.findAllProducers(pageable);
	}

	public Producer updateProducer(Long id, Producer producer) {
		validateProducerName(producer.getName());

		Producer existingProducer = producerRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Produtor", id));

		existingProducer.setName(producer.getName());

		return producerRepository.save(existingProducer);
	}

	public void deleteProducer(Long id) {
		if (!producerRepository.findById(id).isPresent()) {
			throw new ResourceNotFoundException("Produtor", id);
		}

		producerRepository.deleteById(id);
	}

	public ProducerAwardIntervals getProducerAwardIntervals() {

		return producerRepository.getProducerAwardIntervals();
	}

}
