package com.zomato.gateway.service;

import java.util.HashMap;

import com.zomato.gateway.entity.TransactionMaster;
import com.zomato.gateway.paymentrequest.PaymentRequest;

public interface TransactionService {

	public TransactionMaster savePayment(PaymentRequest paymentRequest);
	
	public HashMap<String, String> transactionStatus(String transactionId);

	public HashMap<String, String> refund(String transactionId, float amount);
	
	public HashMap<String, String> updateTransactionStatus(String transactionId,String status);
}
