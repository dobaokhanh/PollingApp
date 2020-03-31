package com.poll.entity.audit;

import java.io.Serializable;
import java.time.Instant;

import javax.persistence.Column;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

public abstract class DateAudit implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@CreatedDate
	@Column(nullable = false, updatable = false)
	private Instant createdAt;
	
	@LastModifiedDate
	@Column(nullable = false)
	private Instant updatedAt;

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
