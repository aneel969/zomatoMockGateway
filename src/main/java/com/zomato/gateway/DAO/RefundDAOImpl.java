package com.zomato.gateway.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zomato.gateway.entity.PaymentDetails;
import com.zomato.gateway.entity.RefundMaster;
import com.zomato.gateway.entity.TransactionMaster;


@Repository
public class RefundDAOImpl implements RefundDAO {

	@Autowired
	SessionFactory session;
	
	public Double getRefundedAmount(String transactionId) {
		
		Session currentSession = session.getCurrentSession();
		String hql="select sum(refundAmount) from RefundMaster where transaction.id=:transactionId";
		Query query= currentSession.createQuery(hql);
		query.setParameter("transactionId", transactionId);
		List<Double> refund = query.list();

		return refund.get(0);
				
	}

	public void save(RefundMaster refund) {
		Session currentSession = session.getCurrentSession();
		currentSession.save(refund);
	}

}
