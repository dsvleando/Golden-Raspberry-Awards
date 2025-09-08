package br.com.outsera.gra.infrastructure.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.outsera.gra.application.port.inbound.MovieUseCase;
import br.com.outsera.gra.application.port.inbound.ProducerUseCase;
import br.com.outsera.gra.application.port.inbound.StudioUseCase;
import br.com.outsera.gra.application.usecase.MovieUseCaseImpl;
import br.com.outsera.gra.application.usecase.ProducerUseCaseImpl;
import br.com.outsera.gra.application.usecase.StudioUseCaseImpl;
import br.com.outsera.gra.domain.repository.MovieRepository;
import br.com.outsera.gra.domain.repository.ProducerRepository;
import br.com.outsera.gra.domain.repository.StudioRepository;
import br.com.outsera.gra.domain.service.MovieDomainService;
import br.com.outsera.gra.domain.service.ProducerDomainService;
import br.com.outsera.gra.domain.service.StudioDomainService;

@Configuration
public class ApplicationConfig {

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

		return modelMapper;
	}

	@Bean
	public MovieDomainService movieDomainService(MovieRepository moviePersistencePort) {
		return new MovieDomainService(moviePersistencePort);
	}

	@Bean
	public StudioDomainService studioDomainService(StudioRepository studioPersistencePort) {
		return new StudioDomainService(studioPersistencePort);
	}

	@Bean
	public ProducerDomainService producerDomainService(ProducerRepository producerPersistencePort) {
		return new ProducerDomainService(producerPersistencePort);
	}

	@Bean
	public MovieUseCase movieUseCase(MovieDomainService movieDomainService) {
		return new MovieUseCaseImpl(movieDomainService);
	}

	@Bean
	public StudioUseCase studioUseCase(StudioDomainService studioDomainService) {
		return new StudioUseCaseImpl(studioDomainService);
	}

	@Bean
	public ProducerUseCase producerUseCase(ProducerDomainService producerDomainService) {
		return new ProducerUseCaseImpl(producerDomainService);
	}
}
