package com.zomato.gateway.paymentmodes;


public class Card {
	//todo enum may be card type
	private String cardNumber;
	private enum type {credit,debit};
	private String cvv;
	private String expiry;
	
	private String type; //credit debit
	public String getCardNumber() {
		return cardNumber;
	}
	public void setCardNumber(String cardNumber) {
		this.cardNumber = cardNumber;
	}
	
	public String getCvv() {
		return cvv;
	}
	public void setCvv(String cvv) {
		this.cvv = cvv;
	}
	public String getExpiry() {
		return expiry;
	}
	public void setExpiry(String expiry) {
		this.expiry = expiry;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	
		
}
