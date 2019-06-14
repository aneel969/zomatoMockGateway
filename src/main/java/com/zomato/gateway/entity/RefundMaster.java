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
@Table(name="refund_master")
public class RefundMaster {
	
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;
	
	@Column(name="refund_amount")
	private float refundAmount;
	
	@ManyToOne
	@JoinColumn(name="transaction_id")
	private TransactionMaster transaction;

	@Column(name="refund_tstamp")
	private String refundTstamp;
	
	
	public String getRefundTstamp() {
		return refundTstamp;
	}

	public void setRefundTstamp(String refundTstamp) {
		this.refundTstamp = refundTstamp;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public float getRefundAmount() {
		return refundAmount;
	}

	public void setRefundAmount(float refundAmount) {
		this.refundAmount = refundAmount;
	}

	public TransactionMaster getTransaction() {
		return transaction;
	}

	public void setTransaction(TransactionMaster transaction) {
		this.transaction = transaction;
	}
			
}
