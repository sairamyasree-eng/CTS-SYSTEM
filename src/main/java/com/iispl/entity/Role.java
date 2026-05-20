package com.iispl.entity;

import java.util.ArrayList;
import java.util.List;

import com.iispl.enums.Status;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

@Entity
public class Role {

	@Id
	@Column(name = "id")
	private String id;

	@Column(name = "roleName")
	private String roleName;

	@Column(name = "description")
	private String description;

	@Column(name = "canCreateBatch")
	private boolean canCreateBatch;

	@Column(name = "canApproveOutward")
	private boolean canApproveOutward;

	@Column(name = "canApproveInward")
	private boolean canApproveInward;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@OneToMany(mappedBy = "role", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private List<User> users = new ArrayList<>();

	public Role() {
	}

	public Role(String id, String roleName, String description, boolean canCreateBatch, boolean canApproveOutward,
			boolean canApproveInward, Status status) {
		this.id = id;
		this.roleName = roleName;
		this.description = description;
		this.canCreateBatch = canCreateBatch;
		this.canApproveOutward = canApproveOutward;
		this.canApproveInward = canApproveInward;
		this.status = status;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isCanCreateBatch() {
		return canCreateBatch;
	}

	public void setCanCreateBatch(boolean canCreateBatch) {
		this.canCreateBatch = canCreateBatch;
	}

	public boolean isCanApproveOutward() {
		return canApproveOutward;
	}

	public void setCanApproveOutward(boolean canApproveOutward) {
		this.canApproveOutward = canApproveOutward;
	}

	public boolean isCanApproveInward() {
		return canApproveInward;
	}

	public void setCanApproveInward(boolean canApproveInward) {
		this.canApproveInward = canApproveInward;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
