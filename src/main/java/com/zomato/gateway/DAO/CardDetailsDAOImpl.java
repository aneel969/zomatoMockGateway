package com.zomato.gateway.DAO;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.zomato.gateway.entity.CardDetails;

@Repository
public class CardDetailsDAOImpl implements CardDetailsDAO {

	@Autowired
	SessionFactory session;

	public void save(CardDetails cardDetails) {
		Session currentSession= session.getCurrentSession();
		currentSession.save(cardDetails);
	}
	
}
