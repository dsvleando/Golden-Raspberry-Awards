package br.com.outsera.gra.application.port.inbound;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Studio;

public interface StudioUseCase {

	public Studio createStudio(Studio studio);

	public Optional<Studio> getStudioById(Long id);

	public Page<Studio> getAllStudios(Pageable pageable);

	public Studio findOrCreateStudioByName(String name);

	public Studio updateStudio(Long id, Studio studio);

	public void deleteStudio(Long id);
}
