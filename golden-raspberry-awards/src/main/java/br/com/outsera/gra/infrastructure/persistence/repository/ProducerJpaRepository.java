package br.com.outsera.gra.infrastructure.persistence.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.outsera.gra.infrastructure.persistence.entity.ProducerEntity;

@Repository
public interface ProducerJpaRepository extends JpaRepository<ProducerEntity, Long> {

	@Query("SELECT p FROM ProducerEntity p WHERE LOWER(p.name) = LOWER(:name)")
	List<ProducerEntity> findByNameEqualsIgnoreCase(@Param("name") String name);

	@Modifying
	@Query(value = "DELETE FROM tb_movie_producer WHERE id_producer = :producerId", nativeQuery = true)
	void deleteMovieLinksByProducerId(@Param("producerId") Long producerId);

	@Query(value = """
			WITH producer_years AS (
			    SELECT DISTINCT p.tx_name as producer_name, m.nr_year
			    FROM tb_movie m
			    JOIN tb_movie_producer mp ON m.id_movie = mp.id_movie
			    JOIN tb_producer p ON mp.id_producer = p.id_producer
			    WHERE m.fl_winner = true
			),
			producer_intervals AS (
			    SELECT
			        py1.producer_name,
			        py1.nr_year as previous_win,
			        py2.nr_year as following_win,
			        (py2.nr_year - py1.nr_year) as "interval"
			    FROM producer_years py1
			    JOIN producer_years py2 ON py1.producer_name = py2.producer_name
			        AND py2.nr_year > py1.nr_year
			    WHERE NOT EXISTS (
			        SELECT 1 FROM producer_years py3
			        WHERE py3.producer_name = py1.producer_name
			        AND py3.nr_year > py1.nr_year
			        AND py3.nr_year < py2.nr_year
			    )
			),
			ranked_intervals AS (
			    SELECT
			        producer_name,
			        "interval",
			        previous_win,
			        following_win,
			        DENSE_RANK() OVER (ORDER BY "interval" ASC) as min_rank,
			        DENSE_RANK() OVER (ORDER BY "interval" DESC) as max_rank,
			        COUNT(*) OVER () as total_count
			    FROM producer_intervals
			)
			SELECT
			    producer_name,
			    "interval",
			    previous_win,
			    following_win,
			    CASE
			        WHEN min_rank = 1 THEN 'min'
			        WHEN max_rank = 1 THEN 'max'
			    END as interval_type
			FROM ranked_intervals
			WHERE min_rank = 1 OR max_rank = 1
			ORDER BY interval_type, "interval"
			""", nativeQuery = true)
	List<Object[]> findProducerAwardIntervals();
}
