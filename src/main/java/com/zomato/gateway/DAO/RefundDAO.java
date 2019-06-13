package com.zomato.gateway.DAO;

import org.springframework.stereotype.Repository;

import com.zomato.gateway.entity.RefundMaster;

@Repository
public interface RefundDAO {
	
	public Double getRefundedAmount(String transactionId);

	public void save(RefundMaster refund);
	
}
