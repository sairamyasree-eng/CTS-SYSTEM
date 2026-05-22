package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "batch_name", nullable = false)
	private String batchName;

	@Column(name = "session_id", nullable = false, insertable = false, updatable = false)
	private String sessionId;

	@Column(name = "created_by_user_id", nullable = false, insertable = false, updatable = false)
	private String createdByUserId;

	@Column(name = "batch_date", nullable = false)
	private LocalDate batchDate;

	@Column(name = "expected_cheque_count")
	private int expectedChequeCount;

	@Column(name = "expected_amount", precision = 15, scale = 2)
	private BigDecimal expectedAmount;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private BatchStatus status;

	@Column(name = "dem_file_path")
	private String demFilePath;

	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = false)
	private CtsSession session;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "created_by_user_id", nullable = false)
	private User createdByUser;

	public Batch() {
	}

	public Batch(String id, String batchName, String sessionId, String createdByUserId, LocalDate batchDate,
			int expectedChequeCount, BigDecimal expectedAmount, BatchStatus status, String demFilePath,
			LocalDateTime createdAt) {
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

	public void setBatchName(String n) {
		this.batchName = n;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String s) {
		this.sessionId = s;
	}

	public String getCreatedByUserId() {
		return createdByUserId;
	}

	public void setCreatedByUserId(String uid) {
		this.createdByUserId = uid;
	}

	public LocalDate getBatchDate() {
		return batchDate;
	}

	public void setBatchDate(LocalDate d) {
		this.batchDate = d;
	}

	public int getExpectedChequeCount() {
		return expectedChequeCount;
	}

	public void setExpectedChequeCount(int c) {
		this.expectedChequeCount = c;
	}

	public BigDecimal getExpectedAmount() {
		return expectedAmount;
	}

	public void setExpectedAmount(BigDecimal a) {
		this.expectedAmount = a;
	}

	public BatchStatus getStatus() {
		return status;
	}

	public void setStatus(BatchStatus s) {
		this.status = s;
	}

	public String getDemFilePath() {
		return demFilePath;
	}

	public void setDemFilePath(String p) {
		this.demFilePath = p;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime t) {
		this.createdAt = t;
	}

	public CtsSession getSession() {
		return session;
	}

	public void setSession(CtsSession s) {
		this.session = s;
	}

	public User getCreatedByUser() {
		return createdByUser;
	}

	public void setCreatedByUser(User u) {
		this.createdByUser = u;
	}
}