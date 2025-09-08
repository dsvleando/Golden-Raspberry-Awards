package br.com.outsera.gra.domain.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Studio;

public interface StudioRepository {

	public Optional<Studio> findByNameEqualsIgnoreCase(String name);

	public Studio save(Studio studio);

	public Optional<Studio> findById(Long id);

	public Page<Studio> findAllStudios(Pageable pageable);

	public void deleteById(Long id);
}
