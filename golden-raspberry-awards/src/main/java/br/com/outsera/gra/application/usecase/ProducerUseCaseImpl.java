package br.com.outsera.gra.application.usecase;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.service.ProducerDomainService;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

public class ProducerUseCaseImpl implements ProducerUseCase {

	private final ProducerDomainService producerDomainService;

	public ProducerUseCaseImpl(ProducerDomainService producerDomainService) {
		this.producerDomainService = producerDomainService;
	}

	@Override
	public Producer findOrCreateProducerByName(String name) {
		return producerDomainService.findOrCreateProducerByName(name);
	}

	@Override
	public Producer createProducer(Producer producer) {
		return producerDomainService.createProducer(producer);
	}

	@Override
	public Optional<Producer> getProducerById(Long id) {
		return producerDomainService.getProducerById(id);
	}

	@Override
	public Page<Producer> getAllProducers(Pageable pageable) {
		return producerDomainService.getAllProducers(pageable);
	}

	@Override
	public Producer updateProducer(Long id, Producer producer) {
		return producerDomainService.updateProducer(id, producer);
	}

	@Override
	public void deleteProducer(Long id) {
		producerDomainService.deleteProducer(id);
	}

	@Override
	public ProducerAwardIntervals getProducerAwardIntervals() {
		return producerDomainService.getProducerAwardIntervals();
	}

}
