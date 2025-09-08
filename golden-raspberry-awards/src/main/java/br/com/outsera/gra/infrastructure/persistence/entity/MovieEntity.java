package br.com.outsera.gra.infrastructure.persistence.entity;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_movie", indexes = { @Index(name = "idx_movie_year", columnList = "nr_year"),
		@Index(name = "idx_movie_winner", columnList = "fl_winner"), })
@Data
public class MovieEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_movie")
	private Long id;

	@Column(name = "tx_title", nullable = false, length = 255)
	private String title;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_movie_studio", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_studio"), indexes = {
			@Index(name = "idx_movie_studio", columnList = "id_movie, id_studio") })
	private List<StudioEntity> studios;

	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "tb_movie_producer", joinColumns = @JoinColumn(name = "id_movie"), inverseJoinColumns = @JoinColumn(name = "id_producer"), indexes = {
			@Index(name = "idx_movie_producer", columnList = "id_movie, id_producer") })
	private List<ProducerEntity> producers;

	@Column(name = "fl_winner")
	private Boolean winner;

	@Column(name = "nr_year", nullable = false)
	private Integer year;

	@Column(name = "dt_created_at", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "dt_updated")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
	}
}
