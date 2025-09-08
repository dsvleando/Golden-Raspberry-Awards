package br.com.outsera.gra.application.port.inbound;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

public interface ProducerUseCase {

	public Producer createProducer(Producer producer);

	public Optional<Producer> getProducerById(Long id);

	public Page<Producer> getAllProducers(Pageable pageable);

	public Producer findOrCreateProducerByName(String name);

	public Producer updateProducer(Long id, Producer producer);

	public void deleteProducer(Long id);

	public ProducerAwardIntervals getProducerAwardIntervals();
}
