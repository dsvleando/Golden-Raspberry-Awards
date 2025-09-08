package br.com.outsera.gra.infrastructure.persistence.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.outsera.gra.infrastructure.persistence.entity.MovieEntity;

@Repository
public interface MovieJpaRepository extends JpaRepository<MovieEntity, Long> {

	Page<MovieEntity> findByWinner(Boolean true1, Pageable pageable);

}
