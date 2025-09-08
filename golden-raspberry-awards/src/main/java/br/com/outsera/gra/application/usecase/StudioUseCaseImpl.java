package br.com.outsera.gra.application.usecase;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.application.port.inbound.StudioUseCase;
import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.service.StudioDomainService;

public class StudioUseCaseImpl implements StudioUseCase {

	private final StudioDomainService studioDomainService;

	public StudioUseCaseImpl(StudioDomainService studioDomainService) {
		this.studioDomainService = studioDomainService;
	}

	@Override
	public Studio findOrCreateStudioByName(String name) {
		return studioDomainService.findOrCreateStudioByName(name);
	}

	@Override
	public Studio createStudio(Studio studio) {
		return studioDomainService.createStudio(studio);
	}

	@Override
	public Optional<Studio> getStudioById(Long id) {
		return studioDomainService.getStudioById(id);
	}

	@Override
	public Page<Studio> getAllStudios(Pageable pageable) {
		return studioDomainService.getAllStudios(pageable);
	}

	@Override
	public Studio updateStudio(Long id, Studio studio) {
		return studioDomainService.updateStudio(id, studio);
	}

	@Override
	public void deleteStudio(Long id) {
		studioDomainService.deleteStudio(id);
	}

}
