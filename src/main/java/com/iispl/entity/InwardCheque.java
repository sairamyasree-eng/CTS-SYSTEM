package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.InwardStatus;
import com.iispl.enums.RepairStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "inward_cheque")
public class InwardCheque {

	@Id

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "cheque_number", nullable = false)
	private String chequeNumber;

	@Column(name = "micr_code")
	private String micrCode;

	@Column(name = "original_micr_code")
	private String originalMicrCode;

	@Column(name = "amount", precision = 15, scale = 2, nullable = false)
	private BigDecimal amount;

	@Column(name = "drawer_bank")
	private String drawerBank;

	@Column(name = "payee_name")
	private String payeeName;

	@Column(name = "account_number")
	private String accountNumber;

	@Column(name = "ifsc_code")
	private String ifscCode;

	@Column(name = "cheque_date")
	private LocalDate chequeDate;

	@Column(name = "dest_branch")
	private String destBranch;

	@Enumerated(EnumType.STRING)
	@Column(name = "inward_status")
	private InwardStatus inwardStatus;

	@Enumerated(EnumType.STRING)
	@Column(name = "repair_status")
	private RepairStatus repairStatus;

	@Column(name = "reject_reason")
	private String rejectReason;

	@Column(name = "refer_note")
	private String referNote;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = true)
	private Batch batch;

	public InwardCheque() {
	}

	public InwardCheque(String chequeNumber, String micrCode, String originalMicrCode, BigDecimal amount,
			String drawerBank, String payeeName, String accountNumber, String ifscCode, LocalDate chequeDate,
			String destBranch, InwardStatus inwardStatus, RepairStatus repairStatus) {
		this.chequeNumber = chequeNumber;
		this.micrCode = micrCode;
		this.originalMicrCode = originalMicrCode;
		this.amount = amount;
		this.drawerBank = drawerBank;
		this.payeeName = payeeName;
		this.accountNumber = accountNumber;
		this.ifscCode = ifscCode;
		this.chequeDate = chequeDate;
		this.destBranch = destBranch;
		this.inwardStatus = inwardStatus;
		this.repairStatus = repairStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getChequeNumber() {
		return chequeNumber;
	}

	public void setChequeNumber(String n) {
		this.chequeNumber = n;
	}

	public String getMicrCode() {
		return micrCode;
	}

	public void setMicrCode(String m) {
		this.micrCode = m;
	}

	public String getOriginalMicrCode() {
		return originalMicrCode;
	}

	public void setOriginalMicrCode(String m) {
		this.originalMicrCode = m;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal a) {
		this.amount = a;
	}

	public String getDrawerBank() {
		return drawerBank;
	}

	public void setDrawerBank(String b) {
		this.drawerBank = b;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String n) {
		this.payeeName = n;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String a) {
		this.accountNumber = a;
	}

	public String getIfscCode() {
		return ifscCode;
	}

	public void setIfscCode(String i) {
		this.ifscCode = i;
	}

	public LocalDate getChequeDate() {
		return chequeDate;
	}

	public void setChequeDate(LocalDate d) {
		this.chequeDate = d;
	}

	public String getDestBranch() {
		return destBranch;
	}

	public void setDestBranch(String b) {
		this.destBranch = b;
	}

	public InwardStatus getInwardStatus() {
		return inwardStatus;
	}

	public void setInwardStatus(InwardStatus s) {
		this.inwardStatus = s;
	}

	public RepairStatus getRepairStatus() {
		return repairStatus;
	}

	public void setRepairStatus(RepairStatus s) {
		this.repairStatus = s;
	}

	public String getRejectReason() {
		return rejectReason;
	}

	public void setRejectReason(String r) {
		this.rejectReason = r;
	}

	public String getReferNote() {
		return referNote;
	}

	public void setReferNote(String n) {
		this.referNote = n;
	}

	public Batch getBatch() {
		return batch;
	}

	public void setBatch(Batch b) {
		this.batch = b;
	}
}