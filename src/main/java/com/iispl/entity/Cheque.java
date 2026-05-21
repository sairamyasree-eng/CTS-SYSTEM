package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cheque")
public class Cheque {

	@Id
	@Column(name = "id", nullable = false, updatable = false)
	private String id;

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

	public Cheque() {
	}

	public Cheque(String id, String chequeNumber, String micrCode, String originalMicrCode, BigDecimal amount,
			String drawerBank, String drawerBranch, String payeeName, String accountNumber, String ifscCode,
			LocalDate chequeDate) {
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
}