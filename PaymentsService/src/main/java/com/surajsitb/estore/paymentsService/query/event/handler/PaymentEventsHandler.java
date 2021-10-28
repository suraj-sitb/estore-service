package com.surajsitb.estore.paymentsService.query.event.handler;

import org.axonframework.eventhandling.EventHandler;
import org.springframework.stereotype.Component;

import com.surajsitb.estore.coreService.core.event.PaymentProcessedEvent;
import com.surajsitb.estore.paymentsService.core.entity.PaymentEntity;
import com.surajsitb.estore.paymentsService.core.repository.PaymentRepository;

@Component
public class PaymentEventsHandler {

	private final PaymentRepository paymentRepository;
	
	public PaymentEventsHandler(PaymentRepository paymentRepository) {
		this.paymentRepository = paymentRepository;
	}
	
	@EventHandler
	public void on(PaymentProcessedEvent paymentProcessedEvent) {
		PaymentEntity paymentEntity = 
				new PaymentEntity(paymentProcessedEvent.getPaymentId(), paymentProcessedEvent.getOrderId());
		
		paymentRepository.save(paymentEntity);
	}
}
