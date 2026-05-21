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
@Table(name = "audit_log")
public class AuditLog {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", updatable = false, nullable = false)
	private Long id;

	@Column(name = "user_id", insertable = false, updatable = false)
	private String userId;

	@Column(name = "session_id", insertable = false, updatable = false)
	private String sessionId;

	@Column(name = "action", nullable = false)
	private String action;

	@Column(name = "entity_type")
	private String entityType;

	@Column(name = "entity_id")
	private String entityId;

	@Column(name = "old_value", length = 2000)
	private String oldValue;

	@Column(name = "new_value", length = 2000)
	private String newValue;

	@Column(name = "ip_address", length = 45)
	private String ipAddress;

	@Column(name = "created_at", updatable = false, nullable = false)
	private LocalDateTime createdAt;

	@Column(name = "description", length = 1000)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = true)
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "session_id", nullable = true)
	private CtsSession session;

	public AuditLog() {
	}

	public AuditLog(String userId, String sessionId, String action, String entityType, String entityId, String oldValue,
			String newValue, String ipAddress, LocalDateTime createdAt, String description) {
		this.userId = userId;
		this.sessionId = sessionId;
		this.action = action;
		this.entityType = entityType;
		this.entityId = entityId;
		this.oldValue = oldValue;
		this.newValue = newValue;
		this.ipAddress = ipAddress;
		this.createdAt = createdAt;
		this.description = description;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String uid) {
		this.userId = uid;
	}

	public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sid) {
		this.sessionId = sid;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String a) {
		this.action = a;
	}

	public String getEntityType() {
		return entityType;
	}

	public void setEntityType(String t) {
		this.entityType = t;
	}

	public String getEntityId() {
		return entityId;
	}

	public void setEntityId(String eid) {
		this.entityId = eid;
	}

	public String getOldValue() {
		return oldValue;
	}

	public void setOldValue(String v) {
		this.oldValue = v;
	}

	public String getNewValue() {
		return newValue;
	}

	public void setNewValue(String v) {
		this.newValue = v;
	}

	public String getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(String ip) {
		this.ipAddress = ip;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime t) {
		this.createdAt = t;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String d) {
		this.description = d;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User u) {
		this.user = u;
	}

	public CtsSession getSession() {
		return session;
	}

	public void setSession(CtsSession s) {
		this.session = s;
	}
}