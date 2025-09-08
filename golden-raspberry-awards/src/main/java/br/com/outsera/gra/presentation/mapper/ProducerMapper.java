package br.com.outsera.gra.presentation.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.outsera.gra.domain.model.Producer;
import br.com.outsera.gra.presentation.dto.request.ProducerRequest;
import br.com.outsera.gra.presentation.dto.response.ProducerAwardIntervalsResponse;
import br.com.outsera.gra.presentation.dto.response.ProducerIntervalResponse;
import br.com.outsera.gra.presentation.dto.response.ProducerResponse;
import br.com.outsera.gra.shared.mapper.EntityMapper;
import br.com.outsera.gra.shared.vo.ProducerAwardIntervals;

@Component
public class ProducerMapper {

	private final EntityMapper entityMapper;

	public ProducerMapper(EntityMapper entityMapper) {
		this.entityMapper = entityMapper;
	}

	public Producer toDomain(ProducerRequest request) {
		return entityMapper.mapObject(request, Producer.class);
	}

	public ProducerAwardIntervalsResponse toResponse(ProducerAwardIntervals domain) {

		return new ProducerAwardIntervalsResponse(entityMapper.mapList(domain.getMin(), ProducerIntervalResponse.class),
				entityMapper.mapList(domain.getMax(), ProducerIntervalResponse.class));
	}

	public ProducerResponse toResponse(Producer producer) {
		return entityMapper.mapObject(producer, ProducerResponse.class);
	}

	public List<ProducerResponse> toResponseList(List<Producer> producers) {
		return entityMapper.mapList(producers, ProducerResponse.class);
	}
}
