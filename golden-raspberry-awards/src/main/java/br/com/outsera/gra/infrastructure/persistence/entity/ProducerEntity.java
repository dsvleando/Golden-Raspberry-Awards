package br.com.outsera.gra.infrastructure.persistence.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "tb_producer", indexes = { @Index(name = "idx_producer_name", columnList = "tx_name") })
@Data
public class ProducerEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id_producer")
	private Long id;

	@Column(name = "tx_name", nullable = false, length = 255, unique = true)
	private String name;

	@Column(name = "dt_created", nullable = false, updatable = false)
	private LocalDateTime createdAt;

	@Column(name = "dt_updated", nullable = false)
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
