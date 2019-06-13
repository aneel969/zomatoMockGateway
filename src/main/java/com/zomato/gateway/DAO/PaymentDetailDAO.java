package com.zomato.gateway.DAO;

import com.zomato.gateway.entity.PaymentDetails;

public interface PaymentDetailDAO {

	public PaymentDetails getPaymentDetails(int modeId,String modeNumber);
	public void save(PaymentDetails paymentDetails);
}
