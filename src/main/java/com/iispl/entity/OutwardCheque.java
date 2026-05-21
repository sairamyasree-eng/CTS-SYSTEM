package com.iispl.entity;

import com.iispl.enums.CheckerStatus;
import com.iispl.enums.IqaStatus;
import com.iispl.enums.MicrStatus;
import com.iispl.enums.RepairStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "outward_cheque")
public class OutwardCheque {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

	@OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
	@MapsId
	@JoinColumn(name = "id")
	private Cheque cheque;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = false)
	private Batch batch;

	@Enumerated(EnumType.STRING)
	@Column(name = "iqa_status")
	private IqaStatus iqaStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "micr_status")
	private MicrStatus micrStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "repair_status")
	private RepairStatus repairStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "checker_status")
	private CheckerStatus checkerStatus;

	@Column(name = "reject_reason")
	private String rejectReason;

	@Column(name = "refer_note")
	private String referNote;

	public OutwardCheque() {
	}

	public OutwardCheque(Cheque cheque, Batch batch, IqaStatus iqaStatus, MicrStatus micrStatus,
			RepairStatus repairStatus, CheckerStatus checkerStatus, String rejectReason, String referNote) {
		this.cheque = cheque;
		this.id = cheque.getId();
		this.batch = batch;
		this.iqaStatus = iqaStatus;
		this.micrStatus = micrStatus;
		this.repairStatus = repairStatus;
		this.checkerStatus = checkerStatus;
		this.rejectReason = rejectReason;
		this.referNote = referNote;
	}

	public String getChequeNumber() {
		return cheque != null ? cheque.getChequeNumber() : null;
	}

	public String getMicrCode() {
		return cheque != null ? cheque.getMicrCode() : null;
	}

	public String getOriginalMicrCode() {
		return cheque != null ? cheque.getOriginalMicrCode() : null;
	}

	public java.math.BigDecimal getAmount() {
		return cheque != null ? cheque.getAmount() : null;
	}

	public String getDrawerBank() {
		return cheque != null ? cheque.getDrawerBank() : null;
	}

	public String getDrawerBranch() {
		return cheque != null ? cheque.getDrawerBranch() : null;
	}

	public String getPayeeName() {
		return cheque != null ? cheque.getPayeeName() : null;
	}

	public String getAccountNumber() {
		return cheque != null ? cheque.getAccountNumber() : null;
	}

	public String getIfscCode() {
		return cheque != null ? cheque.getIfscCode() : null;
	}

	public java.time.LocalDate getChequeDate() {
		return cheque != null ? cheque.getChequeDate() : null;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Cheque getCheque() {
		return cheque;
	}

	public void setCheque(Cheque cheque) {
		this.cheque = cheque;
		if (cheque != null)
			this.id = cheque.getId();
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch batch) {
		this.batch = batch;
	}

	public IqaStatus getIqaStatus() {
		return iqaStatus;
	}

	public void setIqaStatus(IqaStatus iqaStatus) {
		this.iqaStatus = iqaStatus;
	}

	public MicrStatus getMicrStatus() {
		return micrStatus;
	}

	public void setMicrStatus(MicrStatus micrStatus) {
		this.micrStatus = micrStatus;
	}

	public RepairStatus getRepairStatus() {
		return repairStatus;
	}

	public void setRepairStatus(RepairStatus repairStatus) {
		this.repairStatus = repairStatus;
	}

	public CheckerStatus getCheckerStatus() {
		return checkerStatus;
	}

	public void setCheckerStatus(CheckerStatus checkerStatus) {
		this.checkerStatus = checkerStatus;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String rejectReason) {
		this.rejectReason = rejectReason;
	}

	public String getReferNote() {
		return referNote;
	}

	public void setReferNote(String referNote) {
		this.referNote = referNote;
	}
}