package com.iispl.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="audit_log")
public class AuditLog {
	@Id
	@Column(name="id")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private Long id;
	@Column(name="user_id")
	private String userId;
	@Column(name="session_id")
	private String sessionId;
	@Column(name="action")
	private String action;
	@Column(name="entity_type")
	private String entityType;
	@Column(name="entity_id")
	private String entityId;
	@Column(name="old_value")
	private String odlValue;
	@Column(name="new_value")
	private String newValue;
	@Column(name="ip_address")
	private String ipAddress;
	@Column(name="created_at")
	private LocalDateTime createdAt;
	
	
	
	public AuditLog() {	}

	public AuditLog(Long id, String userId, String sessionId, String action, String entityType, String entityId,
			String odlValue, String newValue, String ipAddress, LocalDateTime createdAt, String description) {

		this.id = id;
		this.userId = userId;
		this.sessionId = sessionId;
		this.action = action;
		this.entityType = entityType;
		this.entityId = entityId;
		this.odlValue = odlValue;
		this.newValue = newValue;
		this.ipAddress = ipAddress;
		this.createdAt = createdAt;
		this.description = description;
	}

	@Column(name="description")
	private String description;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="user_id",nullable=true)
	private User user;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="session_id",nullable=true)
	private CtsSession session;



	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String entityType) {
		this.entityType = entityType;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String entityId) {
		this.entityId = entityId;
	}

	public String getOdlValue() {
		return odlValue;
	}

	public void setOdlValue(String odlValue) {
		this.odlValue = odlValue;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String newValue) {
		this.newValue = newValue;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public CtsSession getSession() {
		return session;
	}

	public void setSession(CtsSession session) {
		this.session = session;
	}
	
	
	
}
