package com.zomato.gateway.service;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zomato.gateway.DAO.MerchantDAO;
import com.zomato.gateway.entity.MerchantMaster;

@Service
@Transactional
public class MerchantServiceImpl implements MerchantService {

	@Autowired
	private MerchantDAO merchantDAO;
	
	public boolean isValidMerchant(int merchantId) {
		
		MerchantMaster merchant=merchantDAO.getMerchant(merchantId);
		if(merchant!=null)
			return true;
		return false;
	}

	

}
