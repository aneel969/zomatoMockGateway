package com.zomato.gateway.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.zomato.gateway.entity.PaymentDetails;

@Repository
public class PaymentDetailDAOImpl implements PaymentDetailDAO {

	@Autowired
	SessionFactory sessionFactory;
	public PaymentDetails getPaymentDetails(int modeId,String modeNumber) {
		
		Session currentSession =sessionFactory.getCurrentSession();
		String hql="FROM PaymentDetails where mode.id= :modeId and modeNumber = :modeNumber";
		Query query = currentSession.createQuery(hql);
		query.setParameter("modeId", modeId);
		query.setParameter("modeNumber", modeNumber);
		List<PaymentDetails> paymentDetails = query.list();
		if(paymentDetails != null && paymentDetails.size() >0)
			return paymentDetails.get(0);
		return null;
	}
	
	public void save(PaymentDetails paymentDetails) {
		
		Session currentSession =sessionFactory.getCurrentSession();
		currentSession.save(paymentDetails);
		
	}
}
