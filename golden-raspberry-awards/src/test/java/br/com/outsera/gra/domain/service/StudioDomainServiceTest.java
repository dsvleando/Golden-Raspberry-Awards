package br.com.outsera.gra.domain.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.outsera.gra.domain.model.Studio;
import br.com.outsera.gra.domain.repository.StudioRepository;
import br.com.outsera.gra.shared.exception.ResourceNotFoundException;

@ExtendWith(MockitoExtension.class)
class StudioDomainServiceTest {

	@Mock
	private StudioRepository studioRepository;

	@InjectMocks
	private StudioDomainService studioDomainService;

	@Test
	void createStudio_shouldReturnCreatedStudio() {
		Studio inputStudio = new Studio("Warner Bros.");
		Studio savedStudio = new Studio("Warner Bros.");
		savedStudio.setId(1L);

		when(studioRepository.save(any(Studio.class))).thenReturn(savedStudio);

		Studio result = studioDomainService.createStudio(inputStudio);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(1L);
		assertThat(result.getName()).isEqualTo("Warner Bros.");
		verify(studioRepository).save(inputStudio);
	}

	@Test
	void findOrCreateStudioByName_shouldReturnExistingStudio_whenFound() {
		String studioName = "Paramount Pictures";
		Studio existingStudio = new Studio(studioName);
		existingStudio.setId(2L);

		when(studioRepository.findByNameEqualsIgnoreCase(studioName)).thenReturn(Optional.of(existingStudio));

		Studio result = studioDomainService.findOrCreateStudioByName(studioName);

		assertThat(result).isEqualTo(existingStudio);
		assertThat(result.getId()).isEqualTo(2L);
		verify(studioRepository).findByNameEqualsIgnoreCase(studioName);
		verify(studioRepository, never()).save(any(Studio.class));
	}

	@Test
	void findOrCreateStudioByName_shouldCreateNewStudio_whenNotFound() {
		String studioName = "Universal Pictures";
		Studio newStudio = new Studio(studioName);
		newStudio.setId(3L);

		when(studioRepository.findByNameEqualsIgnoreCase(studioName)).thenReturn(Optional.empty());
		when(studioRepository.save(any(Studio.class))).thenReturn(newStudio);

		Studio result = studioDomainService.findOrCreateStudioByName(studioName);

		assertThat(result).isNotNull();
		assertThat(result.getName()).isEqualTo(studioName);
		assertThat(result.getId()).isEqualTo(3L);
		verify(studioRepository).findByNameEqualsIgnoreCase(studioName);
		verify(studioRepository).save(any(Studio.class));
	}

	@Test
	void getStudioById_shouldReturnStudio_whenFound() {
		Long studioId = 1L;
		Studio studio = new Studio("Warner Bros.");
		studio.setId(studioId);

		when(studioRepository.findById(studioId)).thenReturn(Optional.of(studio));

		Studio result = studioDomainService.getStudioById(studioId).orElse(null);

		assertThat(result).isEqualTo(studio);
		assertThat(result.getId()).isEqualTo(studioId);
		assertThat(result.getName()).isEqualTo("Warner Bros.");
		verify(studioRepository).findById(studioId);
	}

	@Test
	void getStudioById_shouldReturnEmpty_whenNotFound() {
		Long studioId = 999L;
		when(studioRepository.findById(studioId)).thenReturn(Optional.empty());

		Optional<Studio> result = studioDomainService.getStudioById(studioId);
		
		assertThat(result).isEmpty();
		verify(studioRepository).findById(studioId);
	}

	@Test
	void getAllStudios_shouldReturnPaginatedStudios() {
		Studio studio1 = new Studio("Warner Bros.");
		studio1.setId(1L);
		Studio studio2 = new Studio("Paramount Pictures");
		studio2.setId(2L);

		Pageable pageable = PageRequest.of(0, 10);
		PageImpl<Studio> expectedPage = new PageImpl<>(List.of(studio1, studio2), pageable, 2);

		when(studioRepository.findAllStudios(pageable)).thenReturn(expectedPage);

		Page<Studio> result = studioDomainService.getAllStudios(pageable);

		assertThat(result).isNotNull();
		assertThat(result.getContent()).hasSize(2);
		assertThat(result.getContent()).containsExactly(studio1, studio2);
		assertThat(result.getTotalElements()).isEqualTo(2);
		verify(studioRepository).findAllStudios(pageable);
	}

	@Test
	void updateStudio_shouldUpdateAndReturnStudio_whenFound() {
		Long studioId = 1L;
		Studio existingStudio = new Studio("Old Name");
		existingStudio.setId(studioId);

		Studio updateData = new Studio("New Warner Bros.");
		Studio updatedStudio = new Studio("New Warner Bros.");
		updatedStudio.setId(studioId);

		when(studioRepository.findById(studioId)).thenReturn(Optional.of(existingStudio));
		when(studioRepository.save(any(Studio.class))).thenReturn(updatedStudio);

		Studio result = studioDomainService.updateStudio(studioId, updateData);

		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(studioId);
		assertThat(result.getName()).isEqualTo("New Warner Bros.");
		verify(studioRepository).findById(studioId);
		verify(studioRepository).save(any(Studio.class));
	}

	@Test
	void updateStudio_shouldThrow_whenNotFound() {
		Long studioId = 999L;
		Studio updateData = new Studio("Warner Bros.");

		when(studioRepository.findById(studioId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studioDomainService.updateStudio(studioId, updateData))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Estúdio não encontrado com ID: 999");
		verify(studioRepository).findById(studioId);
		verify(studioRepository, never()).save(any(Studio.class));
	}

	@Test
	void deleteStudio_shouldCallRepositoryDelete_whenFound() {
		Long studioId = 1L;
		Studio studio = new Studio("Warner Bros.");
		studio.setId(studioId);

		when(studioRepository.findById(studioId)).thenReturn(Optional.of(studio));

		studioDomainService.deleteStudio(studioId);

		verify(studioRepository).findById(studioId);
		verify(studioRepository).deleteById(studioId);
	}

	@Test
	void deleteStudio_shouldThrow_whenNotFound() {
		Long studioId = 999L;
		when(studioRepository.findById(studioId)).thenReturn(Optional.empty());

		assertThatThrownBy(() -> studioDomainService.deleteStudio(studioId))
				.isInstanceOf(ResourceNotFoundException.class)
				.hasMessageContaining("Estúdio não encontrado com ID: 999");
		verify(studioRepository).findById(studioId);
		verify(studioRepository, never()).deleteById(anyLong());
	}
}
