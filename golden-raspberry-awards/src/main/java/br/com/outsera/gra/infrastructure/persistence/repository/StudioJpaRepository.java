package br.com.outsera.gra.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.outsera.gra.infrastructure.persistence.entity.StudioEntity;

@Repository
public interface StudioJpaRepository extends JpaRepository<StudioEntity, Long> {

	@Query("SELECT s FROM StudioEntity s WHERE LOWER(s.name) = LOWER(:name)")
	List<StudioEntity> findByNameEqualsIgnoreCase(@Param("name") String name);

	@Modifying
	@Query(value = "DELETE FROM tb_movie_studio WHERE id_studio = :studioId", nativeQuery = true)
	void deleteMovieLinksByStudioId(@Param("studioId") Long studioId);
}
