package com.zomato.gateway.paymentrequest;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.zomato.gateway.paymentmodes.Card;
import com.zomato.gateway.paymentmodes.NEFT;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;

public class PaymentRequest {
	
	private String mode;
	private float amount;
	private int merchantId;
	private String redirectUrl;
	
	@JsonTypeInfo(
            use = JsonTypeInfo.Id.NAME,
            include = JsonTypeInfo.As.EXTERNAL_PROPERTY,
            property = "mode"
    )
	//block neft as only card is required for mock payment
	 @JsonSubTypes({
         @JsonSubTypes.Type(value = NEFT.class, name = "1"),
         @JsonSubTypes.Type(value = Card.class, name = "2")
	 })
	private Object details=null;

	public String getMode() {
		return mode;
	}

	public void setMode(String mode) {
		this.mode = mode;
	}

	
	
	public float getAmount() {
		return amount;
	}

	public void setAmount(float amount) {
		this.amount = amount;
	}

	public Object getDetails() {
		return details;
	}

	public void setDetails(Object details) {
		this.details = details;
	}

	public int getMerchantId() {
		return merchantId;
	}

	public void setMerchantId(int merchantId) {
		this.merchantId = merchantId;
	}

	public String getRedirectUrl() {
		return redirectUrl;
	}

	public void setRedirectUrl(String redirectUrl) {
		this.redirectUrl = redirectUrl;
	}
	
}
