package com.blog.app.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter @Setter @ToString
public class BaseEntity {

	@Column(updatable = false)
	private LocalDateTime createdAt;

	@Column(insertable = false)
	private LocalDateTime updatedAt;

	@Column(insertable = false)
	private String updatedBy;

	@PrePersist
	protected void onCreate() {
		this.createdAt = LocalDateTime.now();
	}
}
