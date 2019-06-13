package com.zomato.gateway.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="transaction_logs")
public class TransactionLogs {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)

	private long id;
	
	@ManyToOne
	@JoinColumn(name="transaction_id")
	private TransactionMaster transaction;
	
	@Column(name="previous_status")
	private String previousStatus;
	
	@Column(name="current_status")
	private String currentStatus;

	@Column(name="creation_tstamp")
	private String creationTstamp;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public TransactionMaster getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionMaster transaction) {
		this.transaction = transaction;
	}

	public String getPreviousStatus() {
		return previousStatus;
	}

	public void setPreviousStatus(String previousStatus) {
		this.previousStatus = previousStatus;
	}

	public String getCurrentStatus() {
		return currentStatus;
	}

	public void setCurrentStatus(String currentStatus) {
		this.currentStatus = currentStatus;
	}

	public String getCreationTstamp() {
		return creationTstamp;
	}

	public void setCreationTstamp(String creationTstamp) {
		this.creationTstamp = creationTstamp;
	}
	
	
}
