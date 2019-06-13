package com.zomato.gateway.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zomato.gateway.entity.MerchantMaster;
import com.zomato.gateway.entity.PaymentDetails;

@Repository
public class MerchantDAOImpl implements MerchantDAO {

	@Autowired
	SessionFactory session;
	public MerchantMaster getMerchant(int merchantId) {
		
		Session currentSession = session.getCurrentSession();
		
		String hql ="from MerchantMaster where Id=:merchantId";
		
		Query query = currentSession.createQuery(hql);
		query.setParameter("merchantId", merchantId);
		
		List<MerchantMaster> merchant = query.list();

		if(merchant!=null && merchant.size()>0) {
			
			return merchant.get(0);
		}
		return null;
	}

}

