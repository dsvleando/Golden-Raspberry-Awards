package br.com.outsera.gra.domain.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.repository.StudioRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;

public class StudioDomainService {

	private final StudioRepository studioRepository;

	public StudioDomainService(StudioRepository studioRepository) {
		this.studioRepository = studioRepository;
	}

	public Studio findOrCreateStudioByName(String name) {
		validateStudioName(name);

		Optional<Studio> existingStudio = studioRepository.findByNameEqualsIgnoreCase(name);
		if (existingStudio.isPresent()) {
			return existingStudio.get();
		}

		return createStudio(name);
	}

	public Studio createStudio(Studio studio) {
		return studioRepository.save(studio);
	}

	private Studio createStudio(String name) {
		return createStudio(new Studio(name));
	}

	private void validateStudioName(String name) {
		if (name == null || name.trim().isEmpty()) {
			throw new IllegalArgumentException("Nome do estúdio é obrigatório");
		}
	}

	public Optional<Studio> getStudioById(Long id) {
		return studioRepository.findById(id);
	}

	public Page<Studio> getAllStudios(Pageable pageable) {
		return studioRepository.findAllStudios(pageable);
	}

	public Studio updateStudio(Long id, Studio studio) {
		validateStudioName(studio.getName());

		Studio existingStudio = studioRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Estúdio", id));

		// Atualizar apenas o campo que foi fornecido
		existingStudio.setName(studio.getName());

		return studioRepository.save(existingStudio);
	}

	public void deleteStudio(Long id) {
		if (!studioRepository.findById(id).isPresent()) {
			throw new ResourceNotFoundException("Estúdio", id);
		}

		studioRepository.deleteById(id);
	}

}
