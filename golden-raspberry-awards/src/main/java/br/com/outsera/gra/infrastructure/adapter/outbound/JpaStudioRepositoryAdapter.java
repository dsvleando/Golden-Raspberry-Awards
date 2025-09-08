package br.com.outsera.gra.infrastructure.adapter.outbound;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.repository.StudioRepository;
import br.com.outsera.gra.infrastructure.persistence.entity.StudioEntity;
import br.com.outsera.gra.infrastructure.persistence.repository.StudioJpaRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;
import br.com.outsera.gra.shared.mapper.EntityMapper;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JpaStudioRepositoryAdapter implements StudioRepository {

	private final StudioJpaRepository studioJpaRepository;
	private final EntityMapper entityMapper;

	@Override
	public Optional<Studio> findByNameEqualsIgnoreCase(String name) {
		return studioJpaRepository.findByNameEqualsIgnoreCase(name).stream().findFirst()
				.map(entity -> entityMapper.mapObject(entity, Studio.class));
	}

	@Override
	public Studio save(Studio studio) {
		if (studio.getId() != null) {
			StudioEntity existing = studioJpaRepository.findById(studio.getId())
					.orElseThrow(() -> new ResourceNotFoundException("Estúdio", studio.getId()));
			existing.setName(studio.getName());
			return entityMapper.mapObject(studioJpaRepository.save(existing), Studio.class);
		}

		StudioEntity toCreate = entityMapper.mapObject(studio, StudioEntity.class);
		StudioEntity created = studioJpaRepository.save(toCreate);
		return entityMapper.mapObject(created, Studio.class);
	}

	@Override
	public Optional<Studio> findById(Long id) {
		return studioJpaRepository.findById(id).map(entity -> entityMapper.mapObject(entity, Studio.class));
	}

	@Override
	public Page<Studio> findAllStudios(Pageable pageable) {
		return studioJpaRepository.findAll(pageable).map(entity -> entityMapper.mapObject(entity, Studio.class));
	}

	@Override
	@Transactional
	public void deleteById(Long id) {
		studioJpaRepository.deleteMovieLinksByStudioId(id);
		studioJpaRepository.deleteById(id);
	}

}
