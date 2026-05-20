package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.InwardStatus;
import com.iispl.enums.RepairStatus;

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
@Table(name="inward_cheques")
public class InwardCheque {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;
	
	@Column(name="cheque_number")
	private String chequeNumber;
	
	@Column(name="micr_code")
	private String micrCode;
	
	@Column(name="original_micrcode")
	private String originalMicrCode;
	
	@Column(name="amount")
	private BigDecimal amount;
	
	@Column(name="drawerBank")
	private String drawerBank;
	
	@Column(name="payeeName")
	private String payeeName;
	
	@Column(name="accountNumber")
	private String accountNumber;
	
	@Column(name="ifsc_code")
	private String ifscCode;
	
	@Column(name="cheque_date")
	private LocalDate chequeDate;
	
	@Column(name="dest_branch")
	private String destBranch;
	
	@Column(name="inward_status")
	private InwardStatus inwardStatus;
	
	@Column(name="repair_status")
	private RepairStatus repairStatus;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "batch_id", nullable = true)
	private Batch batch;
	
	
	public InwardCheque() {}


	public InwardCheque(String chequeNumber, String micrCode, String originalMicrCode,
			BigDecimal amount, String drawerBank, String payeeName, String accountNumber, String ifscCode,
			LocalDate chequeDate, String destBranch, InwardStatus inwardStatus, RepairStatus repairStatus) {
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


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}


	public String getChequeNumber() {
		return chequeNumber;
	}


	public void setChequeNumber(String chequeNumber) {
		this.chequeNumber = chequeNumber;
	}


	public String getMicrCode() {
		return micrCode;
	}


	public void setMicrCode(String micrCode) {
		this.micrCode = micrCode;
	}


	public String getOriginalMicrCode() {
		return originalMicrCode;
	}


	public void setOriginalMicrCode(String originalMicrCode) {
		this.originalMicrCode = originalMicrCode;
	}


	public BigDecimal getAmount() {
		return amount;
	}


	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}


	public String getDrawerBank() {
		return drawerBank;
	}


	public void setDrawerBank(String drawerBank) {
		this.drawerBank = drawerBank;
	}


	public String getPayeeName() {
		return payeeName;
	}


	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}


	public String getAccountNumber() {
		return accountNumber;
	}


	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}


	public String getIfscCode() {
		return ifscCode;
	}


	public void setIfscCode(String ifscCode) {
		this.ifscCode = ifscCode;
	}


	public LocalDate getChequeDate() {
		return chequeDate;
	}


	public void setChequeDate(LocalDate chequeDate) {
		this.chequeDate = chequeDate;
	}


	public String getDestBranch() {
		return destBranch;
	}


	public void setDestBranch(String destBranch) {
		this.destBranch = destBranch;
	}


	public InwardStatus getInwardStatus() {
		return inwardStatus;
	}


	public void setInwardStatus(InwardStatus inwardStatus) {
		this.inwardStatus = inwardStatus;
	}


	public RepairStatus getRepairStatus() {
		return repairStatus;
	}


	public void setRepairStatus(RepairStatus repairStatus) {
		this.repairStatus = repairStatus;
	}


	public Batch getBatch() {
		return batch;
	}


	public void setBatch(Batch batch) {
		this.batch = batch;
	}
	
	
	
	
}
