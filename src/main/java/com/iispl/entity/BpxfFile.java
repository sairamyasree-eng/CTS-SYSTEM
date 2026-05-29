package com.iispl.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import jakarta.persistence.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
@Table(name = "bpxf_file")
public class BpxfFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false, updatable = false)
	private Long id;

	@Column(name = "file_name", nullable = false)
	private String fileName;

	@Column(name = "file_path", nullable = false)
	private String filePath;

	@Column(name = "batch_no", nullable = false)
	private String batchNo;

	@Column(name = "branch")
	private String branch;

	@Column(name = "cheque_count")
	private int chequeCount;

	@Column(name = "total_amount", precision = 15, scale = 2)
	private BigDecimal totalAmount;

	@Column(name = "status", nullable = false)
	private String status;

	@Column(name = "parsed_at")
	private LocalDateTime parsedAt;

	public BpxfFile() {

	}

	public BpxfFile(String fileName, String filePath, String batchNo, String branch, int chequeCount,
			BigDecimal totalAmount, String status, LocalDateTime parsedAt) {
		this.fileName = fileName;
		this.filePath = filePath;
		this.batchNo = batchNo;
		this.branch = branch;
		this.chequeCount = chequeCount;
		this.totalAmount = totalAmount;
		this.status = status;
		this.parsedAt = parsedAt;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getBatchNo() {
		return batchNo;
	}

	public void setBatchNo(String batchNo) {
		this.batchNo = batchNo;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public int getChequeCount() {
		return chequeCount;
	}

	public void setChequeCount(int chequeCount) {
		this.chequeCount = chequeCount;
	}

	public BigDecimal getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(BigDecimal totalAmount) {
		this.totalAmount = totalAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDateTime getParsedAt() {
		return parsedAt;
	}

	public void setParsedAt(LocalDateTime parsedAt) {
		this.parsedAt = parsedAt;
	}

}
