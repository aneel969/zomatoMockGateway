package com.zomatotest.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.zomato.gateway.validators.PaymentRequestValidator;
import static org.mockito.Mockito.*;

import com.zomato.gateway.DAO.CardDetailsDAO;
import com.zomato.gateway.DAO.CardDetailsDAOImpl;
import com.zomato.gateway.DAO.PaymentDetailDAO;
import com.zomato.gateway.DAO.PaymentDetailDAOImpl;
import com.zomato.gateway.DAO.RefundDAO;
import com.zomato.gateway.DAO.RefundDAOImpl;
import com.zomato.gateway.DAO.TransactionDAO;
import com.zomato.gateway.DAO.TransactionDAOImpl;
import com.zomato.gateway.controller.PaymentController;
import com.zomato.gateway.entity.TransactionMaster;
import com.zomato.gateway.paymentmodes.Card;
import com.zomato.gateway.paymentrequest.PaymentRequest;
import com.zomato.gateway.service.MerchantService;
import com.zomato.gateway.service.MerchantServiceImpl;
import com.zomato.gateway.service.TransactionService;
import com.zomato.gateway.service.TransactionServiceImpl;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

@RunWith(SpringJUnit4ClassRunner.class)

public class PaymentControllerTest {


	@Test
	public void validateStatusTest() {

		PaymentRequestValidator validator = mock(PaymentRequestValidator.class);
		String redirectUrl = "";
		when(validator.validateRedirectUrl(redirectUrl)).thenCallRealMethod();
		HashMap<String, String> response = new HashMap<String, String>();
		response = validator.validateRedirectUrl(redirectUrl);
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorCode", "2");
		assertEquals(expectedResponse.get("errorCode"), response.get("errorCode"));

	}

	@Test
	public void validateInvalidDateTest() {

		PaymentRequestValidator validator = mock(PaymentRequestValidator.class);

		Card card = new Card();
		card.setCardNumber("1234");
		card.setCvv("123");
		card.setExpiry("12/01/20");
		card.setType("debit");
		when(validator.validateCardRequest(card)).thenCallRealMethod();

		HashMap<String, String> response = new HashMap<String, String>();
		response = validator.validateCardRequest(card);
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorMessage", "invalid date format. expecting dd-mm-yy");
		assertEquals(expectedResponse.get("errorMessage"), response.get("errorMessage"));

	}

	@Test
	public void validateRequest() {

		Card card2 = new Card();
		card2.setCardNumber("1234");
		card2.setCvv("123");
		card2.setExpiry("12-01-20");
		card2.setType("debit");
		PaymentRequestValidator validator = mock(PaymentRequestValidator.class);

		HashMap<String, String> response = new HashMap<String, String>();
		response = validator.validateCardRequest(card2);
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorCode", "0");
		assertEquals(expectedResponse.get("errorCode"), response.get("errorCode"));

	}

	@Mock
	private PaymentDetailDAOImpl paymentDetailsDao;

	@Mock
	private TransactionDAOImpl transactionDAO;

	@Mock
	private RefundDAOImpl refundDAO;

	@Mock
	private CardDetailsDAOImpl cardDetailsDAO;

	private TransactionServiceImpl transactionService;

	@Before
	public void setup() throws Exception {
		transactionService = new TransactionServiceImpl(paymentDetailsDao, transactionDAO, cardDetailsDAO, refundDAO);

	}

	@Test
	public void transactionStatusTest() {

		TransactionMaster transaction = new TransactionMaster();
		
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorMessage", "Invalid transaction Id");
		when(transactionDAO.getTransaction(null)).thenReturn(transaction);
		
		HashMap<String, String> response = new HashMap<String, String>();

		response = transactionService.transactionStatus("5");

		assertEquals(expectedResponse.get("errorMessage"), response.get("errorMessage"));

	}
	
	@Test
	public void alreadyRefundedTransactionRefundTest() {
		
		TransactionMaster transaction = new TransactionMaster();
		transaction.setAmount(10);
		transaction.setId("5");
		transaction.setStatus("refunded");
		HashMap<String, String> response = new HashMap<String, String>();

		response = transactionService.isValidTransaction(transaction);
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorMessage", "failed or refunded transaction");
		
		assertEquals(expectedResponse.get("errorMessage"), response.get("errorMessage"));
	}
	
	@Test
	public void validTransactionRefundTest() {
		
		TransactionMaster transaction = new TransactionMaster();
		transaction.setAmount(10);
		transaction.setId("5");
		transaction.setStatus("partially_refunded");
		HashMap<String, String> response = new HashMap<String, String>();

		response = transactionService.isValidTransaction(transaction);
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorCode", "0");
		
		assertEquals(expectedResponse.get("errorCode"), response.get("errorCode"));
		
	}
	
	
	@Test
	public void inValidRefundAmountTest() {
		
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorMessage", "some part of transaction already refunded");
		
		expectedResponse.put("maxValidRefundAmount","5.0");
		when(refundDAO.getRefundedAmount("5")).thenReturn(5.00);
		HashMap<String, String> response = new HashMap<String, String>();

		response=transactionService.isValidRefund("5",100,10);
		assertEquals(expectedResponse.get("errorMessage"), response.get("errorMessage"));
		assertEquals(expectedResponse.get("maxValidRefundAmount"), response.get("maxValidRefundAmount"));

		
	}
	
	
	@Test
	public void validRefundAmountTest() {
		HashMap<String, String> expectedResponse = new HashMap<String, String>();
		expectedResponse.put("errorCode", "0");
		
		when(refundDAO.getRefundedAmount("5")).thenReturn(0.0);
		HashMap<String, String> response = new HashMap<String, String>();

		response=transactionService.isValidRefund("5",10,100);
		assertEquals(expectedResponse.get("errorCode"), response.get("errorCode"));
	}
	
	
}
