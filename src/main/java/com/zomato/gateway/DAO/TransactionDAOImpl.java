package com.zomato.gateway.DAO;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zomato.gateway.entity.TransactionMaster;
import org.hibernate.query.Query;
import org.hibernate.query.Query;
import org.hibernate.query.Query;
import org.hibernate.query.Query;

@Repository
public class TransactionDAOImpl implements TransactionDAO {

	@Autowired
	SessionFactory sessionFactory;
	public void save(TransactionMaster transaction) {
		
		Session currentSession= sessionFactory.getCurrentSession();
		currentSession.save(transaction);
		
	}
	
	public TransactionMaster getTransaction(String transactionId) {
		Session currentSession=sessionFactory.getCurrentSession();
		String hql = "from TransactionMaster where Id= :transactionId ";
		
		Query query= currentSession.createQuery(hql);
		query.setParameter("transactionId", transactionId);

		List<TransactionMaster> transaction = query.list();
		if(transaction != null && transaction.size() > 0)
			return transaction.get(0);
		return null;
	}
	
	public void updateTransactionStatus(String transactionId, String status) {
		Session currentSession=sessionFactory.getCurrentSession();
		String hql="update TransactionMaster set status=:status where id=:transactionId";
		Query query= currentSession.createQuery(hql);
		query.setParameter("status", status);
		query.setParameter("transactionId", transactionId);
		query.executeUpdate();
	}


}
