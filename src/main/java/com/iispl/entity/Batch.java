package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.iispl.enums.BatchStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "batch")
public class Batch {

	@Id
	@Column(name = "id", nullable = false, insertable = false, updatable = false)
	private String id;

	@Column(name = "batch_name", nullable = false)
	private String batchName;

	@Column(name = "session_id", nullable = false, insertable = false, updatable = false)
	private String sessionId;

	@Column(name = "created_by_user_id", nullable = false, insertable = false, updatable = false)
	private String createdByUserId;

	@Column(name = "batch_date", nullable = false)
	private String batchDate;

	@Column(name = "expected_cheque_count")
	private int expectedChequeCount;

	@Column(name = "expected_amount", precision = 15, scale = 2)
	private BigDecimal expectedAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "Status", nullable = false)
	private BatchStatus status;

	@Column(name = "dem_file_path")
	private String demFilePath;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private CtsSession session;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private User createdByUser;

	public Batch() {
	};

	public Batch(String id, String batchName, String sessionId, String createdByUserId, String batchDate,
			int expectedChequeCount, BigDecimal expectedAmount, BatchStatus status, String demFilePath,
			LocalDateTime createdAt) {
		super();
		this.id = id;
		this.batchName = batchName;
		this.sessionId = sessionId;
		this.createdByUserId = createdByUserId;
		this.batchDate = batchDate;
		this.expectedChequeCount = expectedChequeCount;
		this.expectedAmount = expectedAmount;
		this.status = status;
		this.demFilePath = demFilePath;
		this.createdAt = createdAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(String createdByUserId) {
		this.createdByUserId = createdByUserId;
	}

	public String getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(String batchDate) {
		this.batchDate = batchDate;
	}

	public int getExpectedChequeCount() {
		return expectedChequeCount;
	}

	public void setExpectedChequeCount(int expectedChequeCount) {
		this.expectedChequeCount = expectedChequeCount;
	}

	public BigDecimal getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(BigDecimal expectedAmount) {
		this.expectedAmount = expectedAmount;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public void setStatus(BatchStatus status) {
		this.status = status;
	}

	public String getDemFilePath() {
		return demFilePath;
	}

	public void setDemFilePath(String demFilePath) {
		this.demFilePath = demFilePath;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

}
