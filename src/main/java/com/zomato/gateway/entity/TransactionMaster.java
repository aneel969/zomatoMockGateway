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
@Table(name="transaction_master")
public class TransactionMaster {
	//todo implement cascading 
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	//todo  it shouldnt be auto increment
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "merchant_id")
	private MerchantMaster merchant;
	
	//check float?
	@Column(name="amount")
	private float amount;
	
	@Column(name="status")
	//enum handling
	private String status;
	
	@ManyToOne
	@JoinColumn(name="payment_detail_id")
	private PaymentDetails paymentDetailId;


	@Column(name="transaction_time")
	private String transactionTime;
	
	@Column(name="redirect_url")
	private String redirectUrl;

	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public MerchantMaster getMerchant() {
		return merchant;
	}

	public void setMerchant(MerchantMaster merchant) {
		this.merchant = merchant;
	}

	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public PaymentDetails getPaymentDetailId() {
		return paymentDetailId;
	}

	public void setPaymentDetailId(PaymentDetails paymentDetailId) {
		this.paymentDetailId = paymentDetailId;
	}

	public String getTransactionTime() {
		return transactionTime;
	}

	public void setTransactionTime(String transactionTime) {
		this.transactionTime = transactionTime;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
	
	
}
