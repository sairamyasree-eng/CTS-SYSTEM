package com.iispl.entity;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.iispl.enums.SessionStatus;

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
@Table(name = "cts_session")

public class CtsSession {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@Column(name = "opened_by_user_id", nullable = false, insertable = false, updatable = false)
	private String openedByUserID;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private SessionStatus status;

	@Column(name = "session_date", nullable = false)
	private LocalDate sessionDate;

	@Column(name = "opened_at")
	private LocalDateTime openedAt;

	@Column(name = "closed_at")
	private LocalDateTime closedAt;

	@Column(name = "branch", nullable = false)
	private String branch;

	// hibernate relation MANY TO ONE RELATION
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "opened_by_user_id", nullable = false)
	private User openedByUser;

	// default constructor
	public CtsSession() {
	}

	public CtsSession(String id, String openedByUserID, SessionStatus status, LocalDate sessionDate,
			LocalDateTime openedAt, LocalDateTime closedAt, String branch) {
		super();
		this.id = id;
		this.openedByUserID = openedByUserID;
		this.status = status;
		this.sessionDate = sessionDate;
		this.openedAt = openedAt;
		this.closedAt = closedAt;
		this.branch = branch;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOpenedByUserID() {
		return openedByUserID;
	}

	public void setOpenedByUserID(String openedByUserID) {
		this.openedByUserID = openedByUserID;
	}

	public SessionStatus getStatus() {
		return status;
	}

	public void setStatus(SessionStatus status) {
		this.status = status;
	}

	public LocalDate getSessionDate() {
		return sessionDate;
	}

	public void setSessionDate(LocalDate sessionDate) {
		this.sessionDate = sessionDate;
	}

	public LocalDateTime getOpenedAt() {
		return openedAt;
	}

	public void setOpenedAt(LocalDateTime openedAt) {
		this.openedAt = openedAt;
	}

	public LocalDateTime getClosedAt() {
		return closedAt;
	}

	public void setClosedAt(LocalDateTime closedAt) {
		this.closedAt = closedAt;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public User getOpenedByUser() {
		return openedByUser;
	}

	public void setOpenedByUser(User openedByUser) {
		this.openedByUser = openedByUser;
	}

}
