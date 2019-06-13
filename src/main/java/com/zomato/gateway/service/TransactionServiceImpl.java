package com.zomato.gateway.service;

import java.util.HashMap;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zomato.gateway.DAO.CardDetailsDAO;
import com.zomato.gateway.DAO.PaymentDetailDAO;
import com.zomato.gateway.DAO.RefundDAO;
import com.zomato.gateway.DAO.TransactionDAO;
import com.zomato.gateway.entity.CardDetails;
import com.zomato.gateway.entity.MerchantMaster;
import com.zomato.gateway.entity.ModeMaster;
import com.zomato.gateway.entity.PaymentDetails;
import com.zomato.gateway.entity.RefundMaster;
import com.zomato.gateway.entity.TransactionMaster;
import com.zomato.gateway.paymentmodes.Card;
import com.zomato.gateway.paymentmodes.NEFT;
import com.zomato.gateway.paymentrequest.PaymentRequest;

@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

	@Autowired
	private PaymentDetailDAO paymentDetailsDao;
	
	@Autowired
	private TransactionDAO transactionDAO;

	@Autowired
	private RefundDAO refundDAO;
	
	@Autowired
	private CardDetailsDAO cardDetailsDAO;
	
	public TransactionMaster savePayment(PaymentRequest paymentRequest) {
		int modeId = Integer.parseInt(paymentRequest.getMode());
		
		Card card=(Card) paymentRequest.getDetails();
		String modeNumber=card.getCardNumber();
		PaymentDetails paymentDetail = paymentDetailsDao.getPaymentDetails( modeId,modeNumber);
		if(paymentDetail == null) {
				
			ModeMaster mode = new ModeMaster();
			mode.setId(modeId);
			paymentDetail = new PaymentDetails();
			paymentDetail.setMode(mode);
			paymentDetail.setModeNumber(modeNumber);
			paymentDetailsDao.save(paymentDetail);

			
			CardDetails cardDetails = new CardDetails();
		    cardDetails.setExpiry(card.getExpiry()); 
		    cardDetails.setType(card.getType());
		    cardDetails.setPaymentDetails(paymentDetail);
		    cardDetailsDAO.save(cardDetails);
		}

		MerchantMaster merchant = new MerchantMaster();
		merchant.setId(paymentRequest.getMerchantId());
		TransactionMaster transaction = new TransactionMaster();
		transaction.setAmount(paymentRequest.getAmount());
		transaction.setPaymentDetailId(paymentDetail);
		transaction.setMerchant(merchant);
		transaction.setStatus("processing");
		transaction.setRedirectUrl(paymentRequest.getRedirectUrl());
		transactionDAO.save(transaction);
		
		return transaction;
	}
	
	
	public HashMap<String, String> transactionStatus(String transactionId) {
		
		TransactionMaster transaction =transactionDAO.getTransaction(transactionId);
		HashMap<String,String> response= new HashMap<String, String>();
		if(transaction==null) {
			response.put("errorCode","1");
			response.put("errorMessage", "Invalid transaction Id");
		}else {
			
			response.put("errorCode","0");
			response.put("status",transaction.getStatus());
			response.put("amount",String.valueOf(transaction.getAmount()));
			response.put("transaction_date",transaction.getTransactionTime());
		}
		return response;
	}

	public HashMap<String, String> refund(String transactionId, float amount) {
		
		synchronized (transactionId.intern()) {
		
			TransactionMaster transaction = transactionDAO.getTransaction(transactionId);
			HashMap<String,String> response= new HashMap<String, String>();
			
			//function name smell
			response=isValidTransaction(transaction);
			String errorCode=response.get("errorCode");
			if(!errorCode.equals("0"))
				return response;
			String newStatus=transaction.getStatus();
			float transactionAmount=transaction.getAmount();
			
			//function name smell
			response=isValidRefund(transactionId,amount,transactionAmount);
			errorCode=response.get("errorCode");
			if(!errorCode.equals("0"))
				return response;
			
			float remainingAmount=0;
			if(response.containsKey("maxValidRefundAmount"))
				remainingAmount=Float.valueOf(response.get("maxValidRefundAmount"));
			if(remainingAmount==amount || transactionAmount==amount)
				newStatus="refunded";
			else
				newStatus="partially_refunded";			

			
			RefundMaster refund = new RefundMaster();
			refund= new RefundMaster();
			refund.setRefundAmount(amount);
			refund.setTransaction(transaction);
			refundDAO.save(refund);
			
			response.put("errorCode","0");
			response.put("errorMessage", "success");
			response.put("refundId",refund.getId());
			response.put("transactionId",transactionId);
			response.put("refunded_amount", String.valueOf(refund.getRefundAmount()));
			
			transaction.setStatus(newStatus);
			return response;
		}
		
	}


	private HashMap<String, String> isValidRefund(String transactionId,float refundAmount,float transactionAmount) {
		
		Double refundedAmount=refundDAO.getRefundedAmount(transactionId);
		HashMap<String,String> response= new HashMap<String, String>();
		response.put("errorCode","0");

		if(refundedAmount==null) {
			
			if(refundAmount>transactionAmount) {
				response.put("errorCode","3");
				response.put("errorMessage", "refund amount exceeds transaction amount");
			}
			return response;

		}
			
		double remainingAmount=Math.max(0,transactionAmount-refundedAmount);
		response.put("maxValidRefundAmount",String.valueOf(remainingAmount));
		
		if(refundAmount>remainingAmount) {
			
			response.put("errorCode","4");
			response.put("errorMessage", "some part of transaction already refunded");
			return response;
		}
		
		return response;

	}


	private HashMap<String, String> isValidTransaction(TransactionMaster transaction) {
		
		HashMap<String,String> response= new HashMap<String, String>();
		response.put("errorCode","0");
		if(transaction==null) {
			response.put("errorCode","1");
			response.put("errorMessage", "invalid trasaction");
			return response;
			
		}
		
		String status= transaction.getStatus();
		if(!status.equals("success") && !status.equals("partially_refunded")) { 
			
			response.put("errorCode","2");
			response.put("errorMessage", "failed or refunded transaction");
			return response;
		}
		
		return response;
	}


	public HashMap<String, String> updateTransactionStatus(String transactionId, String status) {
		
		
		HashMap<String, String> response= new HashMap<String, String>();
		TransactionMaster transaction = transactionDAO.getTransaction(transactionId);
		if(transaction==null) {
			response.put("errorCode","1");
			response.put("errorMessage","invalid transaction");
			return response;
		}
		response.put("errorCode","0");
		response.put("transactionId",transactionId);
		response.put("status",status);
		response.put("redirectUrl",transaction.getRedirectUrl());
		transactionDAO.updateTransactionStatus(transactionId, status);
		
		return response;
	}
	
}

