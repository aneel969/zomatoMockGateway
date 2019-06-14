package com.zomato.gateway.validators;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import com.zomato.gateway.paymentmodes.Card;
import com.zomato.gateway.paymentmodes.NEFT;
import com.zomato.gateway.paymentrequest.PaymentRequest;
import com.zomato.gateway.service.MerchantService;
import com.zomato.gateway.service.MerchantServiceImpl;


@Controller
public class PaymentRequestValidator {

	private enum STATUS { pending, success, processing,failed };    

	
	@Autowired 
	private MerchantService merchantService;
	

	public HashMap<String, String> validate(Object obj) {
		PaymentRequest request = (PaymentRequest) obj;
		int success=0;
		
		HashMap<String, String> response= new HashMap<String, String>();
		
		if(request.getAmount()<=0) {
			response.put("errorCode", "2");
			response.put("errorMessage","invalid amount");
			return response;
		}
		if(request.getMode().equals("1"))
			success=validateNEFTRequest((NEFT)request.getDetails());
		else if(request.getMode().equals("2"))
			response=validateCardRequest((Card)request.getDetails());
		String errorCode=response.get("errorCode");
		
		if(errorCode.equals("0")) 
		{
			boolean merchantExists=validateMerchant(request.getMerchantId());
			if(!merchantExists)
			{ 
				response.put("errorCode","3"); 
				response.put("errorMessage","invalid merchant"); 
			}else {
				
				response=validateRedirectUrl(request.getRedirectUrl());
			} 
		}
		return response;
	}
	
	public HashMap<String, String> validateRedirectUrl(String redirectUrl) {
		HashMap<String, String> response= new HashMap<String, String>();
		response.put("errorCode","0");

		if(redirectUrl.length()==0) {
			response.put("errorCode","2");
			response.put("errorMessage","Invalid request url");

		}
		return response;
	}

	public boolean validateMerchant(int merchantId) {
	
		boolean merchantExists=merchantService.isValidMerchant(merchantId);
		return merchantExists;
	}

	public static int validateNEFTRequest(NEFT neft) {
		
		if(neft.getAccountNumber().length()!=4) {
			return 0;
		}
		if(neft.getIfsc().length()!=4) {
			return 0;

		}
		return 1;
	}
	
	
	public static HashMap<String,String> validateCardRequest(Card card) {
			
		HashMap<String, String> response= new HashMap<String, String>();
		
		response.put("errorCode","0");
		try {
			Date expiryDate= new SimpleDateFormat("dd-MM-yy").parse(card.getExpiry());
			Date todayDate = new Date();
			if(todayDate.compareTo(expiryDate)==1)
			{
				response.put("errorCode", "3");
				response.put("errorMessage", "card expired");
				return response;
			}	
			
		} catch (ParseException e) {
			
			response.put("errorCode", "2");
			response.put("errorMessage", "invalid date format. expecting dd-mm-yy");
			
			return response;
		}
		
		
		
		int cardLength=card.getCardNumber().length();
		int cvvLength=card.getCvv().length();
		String cardType=card.getType();
		
		if(!cardType.equals("debit") && !cardType.equals("credit")) {
			response.put("errorCode", "2");
			response.put("errorMessage", "unsupported card type");
			return response;
		
		}
		if(cardLength!=16 || cvvLength!=3) {
			
			response.put("errorCode", "2");
			response.put("errorMessage", "invalid format");	
		}
		return response;
	}
	
	
	public HashMap<String, String> statusValidator(String status) {
		
		HashMap<String, String> response= new HashMap<String, String>();
		 for (STATUS s : STATUS.values()) {
	        if (s.name().equals(status)) {
	    		response.put("errorCode","0");
	            return response;
	        }
		 }
		 
		response.put("errorCode","1");
		response.put("errorMessage","invalid status");
		return response;
	}
	
	public String name() {
		
		return "name";
	}
	public boolean dummyfunction() {
		return true;
	}

}
