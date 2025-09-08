package br.com.outsera.gra.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

public interface ProducerRepository {

	public Optional<Producer> findByNameEqualsIgnoreCase(String name);

	public Producer save(Producer producer);

	public Optional<Producer> findById(Long id);

	public Page<Producer> findAllProducers(Pageable pageable);

	public void deleteById(Long id);

	public ProducerAwardIntervals getProducerAwardIntervals();
}
