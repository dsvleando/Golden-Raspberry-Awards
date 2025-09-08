package br.com.outsera.gra.presentation.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.presentation.dto.request.StudioRequest;
import br.com.outsera.gra.presentation.dto.response.StudioResponse;
import br.com.outsera.gra.shared.mapper.EntityMapper;

@Component
public class StudioMapper {

	private final EntityMapper entityMapper;

	public StudioMapper(EntityMapper entityMapper) {
		this.entityMapper = entityMapper;
	}

	public Studio toDomain(StudioRequest request) {
		return entityMapper.mapObject(request, Studio.class);
	}

	public StudioResponse toResponse(Studio studio) {
		return entityMapper.mapObject(studio, StudioResponse.class);
	}

	public List<StudioResponse> toResponseList(List<Studio> studios) {
		return entityMapper.mapList(studios, StudioResponse.class);
	}
}
