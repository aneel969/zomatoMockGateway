package com.zomato.gateway.tests;

import com.zomato.gateway.controller.PaymentController;
import com.zomato.gateway.paymentmodes.Card;
import com.zomato.gateway.paymentrequest.PaymentRequest;
import com.zomato.gateway.service.TransactionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

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

@RunWith(SpringJUnit4ClassRunner.class)

public class PaymentControllerTest {

   @InjectMocks
   private PaymentController controllerUnderTest;
   
   private MockMvc mockMvc;

   @Before
   public void startService() throws Exception{
       MockitoAnnotations.initMocks(this);
       mockMvc = MockMvcBuilders.standaloneSetup(controllerUnderTest).build();
   }

   
   @Test
   public void testTransactionStatusApi()throws Exception{
	  
	   
       mockMvc.perform(
       get("/gateway/transactionStatus.service?transactionId=1"))
               .andDo(print())
               .andExpect(status().isNotFound());
               /*.andExpect(content().string("\"{errorMessage: \\\"Invalid transaction Id\\\",errorCode: \\\"1\\\"}\""));*/
   }

}
