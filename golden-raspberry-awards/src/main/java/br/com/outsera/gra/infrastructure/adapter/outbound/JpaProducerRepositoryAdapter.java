package br.com.outsera.gra.infrastructure.adapter.outbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.domain.repository.ProducerRepository;
import br.com.outsera.gra.infrastructure.persistence.entity.ProducerEntity;
import br.com.outsera.gra.infrastructure.persistence.repository.ProducerJpaRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.mapper.EntityMapper;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;
import br.com.outsera.gra.shared.vo.ProducerInterval;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaProducerRepositoryAdapter implements ProducerRepository {

	private final ProducerJpaRepository producerJpaRepository;
	private final EntityMapper entityMapper;

	@Override
	public Optional<Producer> findByNameEqualsIgnoreCase(String name) {
		return producerJpaRepository.findByNameEqualsIgnoreCase(name).stream().findFirst()
				.map(entity -> entityMapper.mapObject(entity, Producer.class));
	}

	@Override
	public Producer save(Producer producer) {
		if (producer.getId() != null) {
			ProducerEntity existing = producerJpaRepository.findById(producer.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Produtor", producer.getId()));
			existing.setName(producer.getName());
			return entityMapper.mapObject(producerJpaRepository.save(existing), Producer.class);
		}

		ProducerEntity toCreate = entityMapper.mapObject(producer, ProducerEntity.class);
		ProducerEntity created = producerJpaRepository.save(toCreate);
		return entityMapper.mapObject(created, Producer.class);
	}

	@Override
	public Optional<Producer> findById(Long id) {
		return producerJpaRepository.findById(id).map(entity -> entityMapper.mapObject(entity, Producer.class));
	}

	@Override
	public Page<Producer> findAllProducers(Pageable pageable) {
		return producerJpaRepository.findAll(pageable).map(entity -> entityMapper.mapObject(entity, Producer.class));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		producerJpaRepository.deleteMovieLinksByProducerId(id);
		producerJpaRepository.deleteById(id);
	}

	@Override
	public ProducerAwardIntervals getProducerAwardIntervals() {
		List<Object[]> results = producerJpaRepository.findProducerAwardIntervals();

		List<ProducerInterval> minIntervals = new ArrayList<>();
		List<ProducerInterval> maxIntervals = new ArrayList<>();

		for (Object[] row : results) {
			String producerName = (String) row[0];
			int interval = ((Number) row[1]).intValue();
			int previousWin = ((Number) row[2]).intValue();
			int followingWin = ((Number) row[3]).intValue();
			String intervalType = (String) row[4];

			ProducerInterval producerInterval = new ProducerInterval(producerName, interval, previousWin, followingWin);

			if ("min".equals(intervalType)) {
				minIntervals.add(producerInterval);
			} else if ("max".equals(intervalType)) {
				maxIntervals.add(producerInterval);
			}
		}

		return new ProducerAwardIntervals(minIntervals, maxIntervals);
	}

}