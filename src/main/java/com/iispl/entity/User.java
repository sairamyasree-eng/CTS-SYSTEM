package com.iispl.entity;

import java.time.LocalDate;
import java.util.List;

import com.iispl.enums.Status;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class User {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "username")
	private String username;

	@Column(name = "passwordHash")
	private String passwordHash;

	@Column(name = "fullname")
	private String fullName;

	@Column(name = "branch")
	private String branch;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "created_date")
	private LocalDate createdDate;

	@Column(name = "last_login_at")
	private LocalDate lastLoginAt;

//	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	private List<User> users = new ArrayList<>();

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "role_id")
	private Role role;

	private User() {
	}

	public User(String id, String username, String passwordHash, String fullName, String branch, Status status,
			LocalDate createdDate, LocalDate lastLoginAt) {
		this.id = id;
		this.username = username;
		this.passwordHash = passwordHash;
		this.fullName = fullName;
		this.branch = branch;
		this.status = status;
		this.createdDate = createdDate;
		this.lastLoginAt = lastLoginAt;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public LocalDate getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(LocalDate createdDate) {
		this.createdDate = createdDate;
	}

	public LocalDate getLastLoginAt() {
		return lastLoginAt;
	}

	public void setLastLoginAt(LocalDate lastLoginAt) {
		this.lastLoginAt = lastLoginAt;
	}

//	public List<User> getUsers() {
//		return users;
//	}
//
//	public void setUsers(List<User> users) {
//		this.users = users;
//	}

	public Role getRole() {
		return role;
	}

	public void setRole(Role role) {
		this.role = role;
	}

}
