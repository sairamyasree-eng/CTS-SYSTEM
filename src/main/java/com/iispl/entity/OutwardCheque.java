package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import com.iispl.enums.CheckerStatus;
import com.iispl.enums.IqaStatus;
import com.iispl.enums.MicrStatus;
import com.iispl.enums.RepairStatus;

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
@Table(name = "outward_cheque")
public class OutwardCheque {
	  @Id
	  @Column(name = "id")
	  private String id;
	  
	  @ManyToOne(fetch = FetchType.LAZY)
	  @JoinColumn(name = "batch_id", nullable = false)
	  private Batch batch;
	  
	  @Column(name = "cheque_number")
	  private String chequeNumber;
	  @Column(name = "micr_code")
	  private String micrCode;
	  @Column(name = "original_micr_code")
	  private String originalMicrCode;
	  @Column(name = "amount")
	  private BigDecimal amount; 
	  @Column(name = "drawer_bank")
	  private String drawerBank;
	  @Column(name = "drawer_branch")
	  private String drawerBranch;
	  @Column(name = "payee_name")
	  private String payeeName;
	  @Column(name = "account_number")
	  private String accountNumber;
	  @Column(name = "ifsc_code")
	  private String ifscCode;
	  @Column(name = "cheque_date")
	  private LocalDate chequeDate;
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
	  
	  public OutwardCheque() { }

	  public OutwardCheque(String id, String chequeNumber, String micrCode, String originalMicrCode,
			BigDecimal amount, String drawerBank, String drawerBranch, String payeeName, String accountNumber,
			String ifscCode, LocalDate chequeDate, IqaStatus iqaStatus, MicrStatus micrStatus,
			RepairStatus repairStatus, CheckerStatus checkerStatus, String rejectReason, String referNote) {
		this.id = id;
		this.chequeNumber = chequeNumber;
		this.micrCode = micrCode;
		this.originalMicrCode = originalMicrCode;
		this.amount = amount;
		this.drawerBank = drawerBank;
		this.drawerBranch = drawerBranch;
		this.payeeName = payeeName;
		this.accountNumber = accountNumber;
		this.ifscCode = ifscCode;
		this.chequeDate = chequeDate;
		this.iqaStatus = iqaStatus;
		this.micrStatus = micrStatus;
		this.repairStatus = repairStatus;
		this.checkerStatus = checkerStatus;
		this.rejectReason = rejectReason;
		this.referNote = referNote;
	  }

	  public String getId() {
		  return id;
	  }

	  public void setId(String id) {
		  this.id = id;
	  }

	  public Batch getBatch() {
		  return batch;
	  }

	  public void setBatch(Batch batch) {
		  this.batch = batch;
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

	  public String getDrawerBranch() {
		  return drawerBranch;
	  }

	  public void setDrawerBranch(String drawerBranch) {
		  this.drawerBranch = drawerBranch;
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
