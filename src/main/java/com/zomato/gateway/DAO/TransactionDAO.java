package com.zomato.gateway.DAO;

import java.util.List;

import com.zomato.gateway.entity.TransactionMaster;

public interface TransactionDAO {

	void save(TransactionMaster transaction);

	TransactionMaster getTransaction(String transactionId);
	
	void updateTransactionStatus(String transactionId, String status);
}
