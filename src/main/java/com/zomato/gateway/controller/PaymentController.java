package com.zomato.gateway.controller;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zomato.gateway.entity.TransactionMaster;
import com.zomato.gateway.paymentmodes.NEFT;
import com.zomato.gateway.paymentrequest.PaymentRequest;
import com.zomato.gateway.service.MerchantService;
import com.zomato.gateway.service.TransactionService;
import com.zomato.gateway.validators.PaymentRequestValidator;
@RestController
@RequestMapping("/gateway")
public class PaymentController {

	@Autowired
	private TransactionService transactionService;
	
	@Autowired
	private PaymentRequestValidator validator;
	
	@Value("${mock.bank.url}")
	String mockBankUrl;
	@RequestMapping(value="/payment",method=RequestMethod.POST)
	private ResponseEntity processPayment(@RequestBody PaymentRequest paymentRequest) throws JsonParseException, JsonMappingException, IOException{
		
		HashMap<String, String> response= new HashMap<String, String>();
		ResponseEntity responseEntity = null;
		response.put("errorCode","0");		
		try { 
			response=validator.validate(paymentRequest);

		}catch (NullPointerException e) {
		
			e.printStackTrace();
			response.put("errorCode","1");
            response.put("errorMessage","Invalid Request");
            responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
		}catch (Exception e) {
			
			response.put("errorCode","1");
            response.put("errorMessage","Server Error");
            responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		try {
			String errorCode=response.get("errorCode");
			if(errorCode.equals("0")) {
				TransactionMaster transaction=transactionService.savePayment(paymentRequest);
				String transactionId=transaction.getId();

				response.put("errorCode","0");
				response.put("transactionId",transactionId);
				response.put("status",transaction.getStatus());
				response.put("bankUrl",mockBankUrl+"?transactionId="+transactionId);
	            responseEntity = new ResponseEntity(response, HttpStatus.OK);
				
			}else {
				responseEntity= new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			response.put("errorCode","4");
			response.put("errorMessage","Server Error");
	        responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
		return responseEntity;
	}

	@RequestMapping(value="/transactionStatus",method=RequestMethod.POST)
	private ResponseEntity getTransactionStatus(@RequestParam String transactionId) {
		
		HashMap<String,String> response;
		ResponseEntity responseEntity = null;

		try {
			response=transactionService.transactionStatus(transactionId);
			String errorCode=response.get("errorCode");
			if(errorCode.equals("0"))
		        responseEntity = new ResponseEntity(response, HttpStatus.OK);
			else
		        responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			
		} catch (Exception e) {
			response=new HashMap<String, String>();
			response.put("errorCode","4");
			response.put("errorMessage","Server Error");
	        responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return responseEntity;
	}
	
	@RequestMapping(value="refund",method=RequestMethod.POST)
	private ResponseEntity refundTransaction(@RequestParam String transactionId,@RequestParam float amount) {
		
		HashMap<String,String> response ;
		ResponseEntity responseEntity = null;
		
		if(amount<=0)
		{
			response=new HashMap<String, String>();
			response.put("errorCode","1");
			response.put("errorMessage","invalid amount");
	        responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			
		}else {
			try {
				response=transactionService.refund(transactionId,amount);
				String errorCode=response.get("errorCode");
				if(errorCode.equals("0"))
			        responseEntity = new ResponseEntity(response, HttpStatus.OK);
				else
			        responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
				
			} catch (Exception e) {
				e.printStackTrace();
				response=new HashMap<String, String>();
				response.put("errorCode","4");
				response.put("errorMessage","Server Error");
		        responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);

			}
		}
		return responseEntity;
		
	}
	
	
	@RequestMapping(value="updateTransaction",method=RequestMethod.POST)
	@SuppressWarnings("rawtypes") 
	private ResponseEntity updateTransactionStatus(@RequestParam String transactionId,@RequestParam String status){
		
		HashMap<String, String> response=null;
		ResponseEntity responseEntity = null;
		
		response=validator.statusValidator(status);
		String errorCode=response.get("errorCode");

		try {
			if(errorCode.equals("0")) {
				response=transactionService.updateTransactionStatus(transactionId, status);
				errorCode=response.get("errorCode");
				if(errorCode.equals("0"))
			        responseEntity = new ResponseEntity(response, HttpStatus.OK);
				else
			        responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}else {
		        responseEntity = new ResponseEntity(response, HttpStatus.BAD_REQUEST);
			}	
				
		} catch (Exception e) {
			
			response.put("errorCode","1");
			response.put("errorMessage","Server Error");
	        responseEntity = new ResponseEntity(response, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return responseEntity;
		
	}
	
}

